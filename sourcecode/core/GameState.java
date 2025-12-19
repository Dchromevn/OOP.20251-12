package core;

import player.Player;
import notification.NotificationManager;
import eventSystem.RandomEventManager;
import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Farm farm;
    private Player player;
    private NotificationManager notificationManager;
    private RandomEventManager eventManager;
    public GameState(Farm farm, Player player, NotificationManager notificationManager, RandomEventManager eventManager) {
        this.farm = farm;
        this.player = player;
        this.notificationManager = notificationManager;
        this.eventManager = eventManager;
    }
    public Farm getFarm() {
        return farm;
    }
    public Player getPlayer() {
        return player;
    }
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
    public RandomEventManager getEventManager() {
        return eventManager;
    }
}