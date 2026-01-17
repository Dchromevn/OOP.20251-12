package controller.game;

import model.core.*;
import model.crops.Crop;
import model.notification.NotificationManager;
import model.player.*;
import utility.*;
import model.exceptions.*;
import model.resourceManagement.*;
import service.eventSystem.RandomEventManager;
public class PlayerController {
    private Player player;
    private Farm farm;
    private Store shop;
    private NotificationManager notificationManager;
    private UIManager uiManager;
    public PlayerController(Player player, Farm farm, NotificationManager notificationManager, Store shop, RandomEventManager eventManager,UIManager uiManager) {
        this.player = player;
        this.farm = farm;
        this.notificationManager = notificationManager;
        this.shop = shop;
        this.uiManager = uiManager;
    }
    protected void setUIManager(UIManager uiManager) {
        if (this.uiManager != null) {
            throw new IllegalStateException("UIManager already set!");
        }
        this.uiManager = uiManager;
    }
    private void notifyFeedback(String message, NotificationType type) {
        notificationManager.addNotification(message,type,farm.getCurrentDay());
        uiManager.showNotification(message,farm.getCurrentDay());
    }
    public Farm getFarm() { return this.farm; }
    public Player getPlayer() { return this.player; }
    public NotificationManager getNotificationManager() { return this.notificationManager; }

    public boolean plantCrop(CropType type, Point position) {
        try {
            FarmCell cell = farm.getCell(position);
            player.getInventory().removeSeed(type, 1);
            Crop crop = CropFactory.createCrop(type, position);
            cell.plantCrop(crop);
            notifyFeedback("Successfully planted " + type.getCropName(), NotificationType.SUCCESS);
            return true;

        } catch (InvalidPositionException |CellOccupiedException |NotEnoughResourceException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }
    public boolean waterCrop(Point position, int amount) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();
            if (crop.isHarvestable()) {
                notifyFeedback(crop.getCropType().getCropName() + " is harvestable. Cannot water",NotificationType.WARNING);
                return false;
            }
            if (crop.isDead()) {
                notifyFeedback("Cannot water dead crop! Recycle it instead.",NotificationType.WARNING);
                return false;
            }
            if (crop.isWaterFull()) {
                notifyFeedback(crop.getCropType().getCropName() +" is fully watered!", NotificationType.WARNING);
                return false;
            }
            player.getInventory().useWater(amount);
            crop.water(amount);
            notifyFeedback("Watered " + crop.getCropType().getCropName() + " (+" + amount + " water)", NotificationType.SUCCESS);
            return true;
        } catch (InvalidPositionException |NotEnoughResourceException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }

    public boolean fertilizeCrop(Point position, int amount) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();

            if (crop.isHarvestable()) {
                notifyFeedback(crop.getCropType().getCropName() + " is harvestable. Cannot fertilize",NotificationType.WARNING);
                return false;
            }
            if (crop.isDead()) {
                notifyFeedback("Cannot fertilize dead crop! Recycle it instead.",NotificationType.WARNING);
                return false;
            }
            if (crop.isFertilizerFull()) {
                notifyFeedback(crop.getCropType().getCropName() +" is fully fertilized!", NotificationType.WARNING);
                return false;
            }
            player.getInventory().useFertilizer(amount);
            crop.fertilize(amount);
            notifyFeedback("Fertilized " + crop.getCropType().getCropName() + " (+" + amount + " fertilizer)", NotificationType.SUCCESS);
            return true;
        } catch (InvalidPositionException | NotEnoughResourceException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }
    public boolean harvestCrop(Point position) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();
            if (!crop.isHarvestable()) {
                notifyFeedback(crop.getCropType().getCropName() + " is not ready for harvesting", NotificationType.WARNING);
                return false;
            }
            int moneyEarned = crop.harvest();
            player.getInventory().earnMoney(moneyEarned);
            cell.removeCrop();
            String message = "Harvested " + crop.getCropType().getCropName() + " (+" + moneyEarned + "$)";
            notifyFeedback(message, NotificationType.SUCCESS);

            return true;
        } catch (InvalidPositionException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }
    public boolean cureCrop(Point position) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();
            if (crop.isDead()) {
                notifyFeedback("Cannot cure dead crop! Recycle it.", NotificationType.WARNING);
                return false;
            }
            if (crop.getHealth() >= 100) {
                notifyFeedback("Crop is already healthy!", NotificationType.WARNING);
                return false;
            }
            player.getInventory().useMedicine(1);

            int remainingMeds = player.getInventory().getMedicine();

            crop.recoverHealth(30);

            notifyFeedback("Used Medicine on " + crop.getCropType().getCropName() +" (Left: " + remainingMeds + ")",NotificationType.SUCCESS);

            return true;

        } catch (NotEnoughResourceException | InvalidPositionException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }
    public boolean buyItem(StoreItem item, int amount) {
        try {
            shop.sellToPlayer(item, player.getInventory(), amount);

            String message = "Purchased " + amount + " " + item.getName();
            notifyFeedback(message, NotificationType.SUCCESS);
            return true;

        } catch (NotEnoughResourceException e) {
            notifyFeedback("Failed to buy " + item.getName() + ": " + e.getMessage(), NotificationType.ERROR);
            return false;
        }
    }


    public void recycleCrop(Point position) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();
            if (crop.canBeRecycled()) {
                int fertilizerGained = crop.recycle();
                player.getInventory().gainFertilizer(fertilizerGained);
                cell.removeCrop();
                notifyFeedback("Recycled crop. Gained " + fertilizerGained + " fertilizer.", NotificationType.SUCCESS);
            } else {
                notifyFeedback("Crop is still healthy, cannot recycle.", NotificationType.WARNING);
            }
        } catch (InvalidPositionException e) {
            notifyFeedback(e.getMessage(), NotificationType.ERROR);

        }
    }
}