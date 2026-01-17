package albums.analytics_service;

import albums.AlbumRatingRequest;
import albums.AlbumRatingResponse;
import albums.AnalyticsServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void calculateAlbumRating(AlbumRatingRequest request, StreamObserver<AlbumRatingResponse> responseObserver) {
        int score = (int) (Math.random() * 100);

        String verdict;
        if(score < 30) {
            verdict = "VERY BAD";
        } else if (score < 50) {
            verdict = "BAD";
        } else {
            verdict = score > 70 ? "GOOD" : "AVERAGE";
        }

        AlbumRatingResponse response = AlbumRatingResponse.newBuilder()
                .setAlbumId(request.getAlbumId())
                .setRatingScore(score)
                .setVerdict(verdict)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

