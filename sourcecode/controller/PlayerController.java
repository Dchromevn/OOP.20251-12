package controller;
import core.*;
import eventSystem.RandomEventManager;
import notification.NotificationManager;
import player.Player;
import utility.*;
import exceptions.*;
import resourceManagement.ResourceManager;
public class PlayerController {

    private Player player;
    private Farm farm;
    private ResourceManager shop;
    private NotificationManager notificationManager;
    public PlayerController(Player player, Farm farm, NotificationManager notificationManager,ResourceManager shop) {
        this.player = player;
        this.farm = farm;
        this.notificationManager=notificationManager;
        this.shop=shop;
    }
    public boolean plantCrop(CropType type, Point position) {
        try {
            FarmCell cell = farm.getCell(position);  
            Crop crop = CropFactory.createCrop(type, position);

            cell.plantCrop(crop);     
            player.getInventory().removeSeed(type, 1);

            return true;

        } catch (InvalidPositionException |
                 CellOccupiedException |
                 NotEnoughResourceException e) {

            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean waterCrop(Point position, int amount) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();
            player.getInventory().useWater(amount);
            crop.water(amount);
            return true;
        } catch (InvalidPositionException |
                 NotEnoughResourceException |
                 IllegalStateException e) {

            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean fertilizeCrop(Point position, int amount) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop =  cell.requireCrop();

            player.getInventory().useFertilizer(amount);
            crop.fertilize(amount);
            return true;
        } catch (InvalidPositionException |
                 NotEnoughResourceException |
                 IllegalStateException e) {

            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean harvestCrop(Point position) {
        try {
            FarmCell cell = farm.getCell(position);
            Crop crop = cell.requireCrop();

            if (!crop.isHarvestable()) {
                System.out.println(
                    crop.getCropType().getCropName() + " is not ready for harvesting"
                );
                return false;
            }

            int moneyEarned = crop.harvest();
            player.getInventory().earnMoney(moneyEarned);
            cell.removeCrop();

            return true;

        } catch (InvalidPositionException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean buySeed(CropType type, int amount) {
        boolean success = shop.sellSeedToPlayer(player.getInventory(),type, amount);
        if (success) {
            // 2. Sửa thông báo từ "water units" thành "seeds" cho đúng ngữ cảnh
            notificationManager.addNotification(
                    "Purchased " + amount + " " + type.getCropName() + " seeds",
                    NotificationType.SUCCESS,
                    farm.getCurrentDay()
            );
        } else {
            notificationManager.addNotification(
                    "Failed to buy seeds: Not enough money",
                    NotificationType.ERROR,
                    farm.getCurrentDay()
            );
        }
        return success;
    }
    public boolean buyWater(int amount) {
        boolean success = shop.sellWaterToPlayer(player.getInventory(), amount);
        if (success) {
            notificationManager.addNotification("You have bought " + amount + " water units", NotificationType.SUCCESS, farm.getCurrentDay());
        } else {
            notificationManager.addNotification("Failed to buy, not enough money", NotificationType.ERROR, farm.getCurrentDay());
        }
        return success;
    }
    public boolean buyFertilizer(int amount) {
        boolean success = shop.sellFertilizerToPlayer(player.getInventory(), amount);
        if (success) {
            notificationManager.addNotification("You have bought " + amount + " fertilizer units", NotificationType.SUCCESS, farm.getCurrentDay());
        } else {
            notificationManager.addNotification("Failed to buy, not enough money", NotificationType.ERROR, farm.getCurrentDay());
        }
        return success;
    }

    public void nextDay(RandomEventManager eventManager) {
        farm.advanceDay(eventManager);
    }
    public void displayInventory() {
    	player.getInventory().showInventory();
    }
    public void printPlayerStatus() {
    	System.out.println(player);
    }
    public void printFarmStatus() {
    	farm.printFarm();
    }
}


