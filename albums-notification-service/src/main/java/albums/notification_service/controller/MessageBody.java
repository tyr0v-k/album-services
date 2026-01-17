package albums.notification_service.controller;

public class MessageBody {
    private String userId;
    private String message;

    public MessageBody(String userId, String message){
        this.userId = userId;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
