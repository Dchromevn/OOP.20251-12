package notification;

import utility.NotificationType;

public class Notification {
    private String message;
    private NotificationType type;
    private boolean isRead;
    private int gameDay;
    public Notification(String message, NotificationType type, int gameDay){
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.gameDay = gameDay;
    }

    public void markAsRead(){
        this.isRead = true;
    }
    public int getGameDay() {
        return gameDay;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public boolean isRead() {
        return isRead;
    }
    @Override
    public String toString() {
        String readStatus = isRead ? " " : "[NEW!]";

        return String.format("%s [Day %d] %s %s ",
                readStatus,
                gameDay,
                type.getNotificationName(),
                message
        );
    }
}