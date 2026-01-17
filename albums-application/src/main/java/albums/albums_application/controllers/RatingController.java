package albums.albums_application.controllers;

import albums.AlbumRatingRequest;
import albums.AnalyticsServiceGrpc;
import albums.albums_application.config.RabbitMQConfig;
import albums.albums_application.service.AlbumService;
import events.AlbumRatedEvent;
import io.grpc.StatusRuntimeException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
public class RatingController {

    private final AlbumService albumService;

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public RatingController(AlbumService albumService, RabbitTemplate rabbitTemplate) {
        this.albumService = albumService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/albums/{id}/rate")
    public String rateAlbum(@PathVariable String id) {
        try{
            if (albumService.findAlbumById(id) != null){
                var request = AlbumRatingRequest.newBuilder().setAlbumId(id).build();
                var gRpcResponse = analyticsStub.calculateAlbumRating(request);
                var event = new AlbumRatedEvent(gRpcResponse.getAlbumId(), gRpcResponse.getRatingScore(), gRpcResponse.getVerdict());

                rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

                return "Вычисленный рейтинг: " + gRpcResponse.getRatingScore();
            }
            else {
                throw new RuntimeException();
            }
        }
        catch (StatusRuntimeException e){
            System.err.println(e);
            return "Вычисленный рейтинг: -1";
        }
    }
}