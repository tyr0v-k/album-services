package albums.audit_service.listeners;

import com.rabbitmq.client.Channel;
import events.AlbumCreatedEvent;
import events.AlbumDeletedEvent;
import events.AlbumRatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AlbumEventListener {
    private static final Logger log = LoggerFactory.getLogger(AlbumEventListener.class);
    private static final String EXCHANGE_NAME = "albums-exchange";
    private static final String QUEUE_NAME = "notification-queue";
    private final Set<String> processedAlbumCreations = ConcurrentHashMap.newKeySet();

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = QUEUE_NAME,
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
                            }),
                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
                    key = "album.created"
            )
    )
    public void handleAlbumCreatedEvent(@Payload AlbumCreatedEvent event, Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        if (!processedAlbumCreations.add(event.albumId())) {
            log.warn("Дубликат события получен для альбома с ID: {}", event.albumId());
            channel.basicAck(deliveryTag, false);
            return;
        }
        try {
            log.info("Получен AlbumCreatedEvent: {}", event);
            if (event.title() != null && event.title().equalsIgnoreCase("CRASH")) {
                throw new RuntimeException("Тестируем DLQ");
            }
            log.info("Уведомление отправлено для нового альбома '{}'!", event.title());
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Ошибка в обработке события: {}. Отправляем в DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = QUEUE_NAME,
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
                            }),
                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
                    key = "album.deleted"
            )
    )
    public void handleAlbumDeletedEvent(@Payload AlbumDeletedEvent event, Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Пришел AlbumDeletedEvent: {}", event);
            log.info("Уведомления отменены для альбома с ID: {}!", event.albumId());
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Ошибка в обработке события: {}. Отправляем в DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "notification-queue.dlq", durable = "true"),
                    exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
                    key = "dlq.notifications"
            )
    )
    public void handleDlqMessages(Object failedMessage) {
        log.error("Внимание! Новое сообщение в DLQ: {}", failedMessage);
    }




    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.audit.analytics", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void handleRating(AlbumRatedEvent event) {
        log.info("Уведомление: у альбома {} новый рейтинг: {}, оценка: {}", event.albumId(), event.score(), event.verdict());
    }
}