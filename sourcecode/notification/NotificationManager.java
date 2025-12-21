package notification;

import utility.NotificationType;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationManager implements Serializable {
    private LinkedList<Notification> notifications;
    private static final int HISTORY_LIMIT = 100;

    public NotificationManager() {
        this.notifications = new LinkedList<>();
    }

    public void addNotification(Notification notification) {
        if (notification == null) {
            System.out.println("Cannot add null notification!");
            return;
        }
        notifications.add(notification);
        if (notifications.size() > HISTORY_LIMIT) {
            notifications.removeFirst();
        }
        System.out.println(notification.toString());
    }

    public void addNotification(String message, NotificationType type, int gameDay) {
        Notification notification = new Notification(message, type, gameDay);
        addNotification(notification);
    }
}
