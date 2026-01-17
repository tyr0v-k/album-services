package albums.notification_service.controller;

import albums.notification_service.websocket.NotificationHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationHandler handler;

    public NotificationController(NotificationHandler handler) {
        this.handler = handler;
    }


    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody String message) {
        handler.broadcast(message);
        return ResponseEntity.ok("Отправлено широковещательное сообщение");
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", handler.getActiveConnections()
        ));
    }

    @GetMapping("/message")
    public ResponseEntity<String> message(@RequestBody MessageBody messageBody) {
        handler.messageForUser(messageBody.getUserId(), String.format(
                "{\"type\": \"MESSAGE_FOR_USER\", \"message\": \"%s\"}",
                messageBody.getMessage()
        ));
        return ResponseEntity.ok(
                "Сообщение отправлено пользователю с ID " + messageBody.getUserId()
        );
    }
}