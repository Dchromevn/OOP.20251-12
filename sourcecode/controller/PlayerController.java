package controller;
import core.*;
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
            if (crop.isHarvestable()) {
                notificationManager.addNotification(
                        crop.getCropType().getCropName() + " is harvestable. Cannot water",
                        NotificationType.WARNING,
                        farm.getCurrentDay()
                );
                return false;
            }
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
            if (crop.isHarvestable()) {
                notificationManager.addNotification(
                        crop.getCropType().getCropName() + " is harvestable. Cannot fertilize",
                        NotificationType.WARNING,
                        farm.getCurrentDay()
                );
                return false;
            }
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

    public void buySeed(CropType type, int amount) {
        try{
            shop.sellSeedToPlayer(player.getInventory(),type, amount);
            notificationManager.addNotification(
                    "Purchased " + amount + " " + type.getCropName() + " seeds",
                    NotificationType.SUCCESS,
                    farm.getCurrentDay()
            );
        } catch (NotEnoughResourceException e) {
            notificationManager.addNotification(
                    "Failed to buy seeds: Not enough money",
                    NotificationType.ERROR,
                    farm.getCurrentDay()
            );
        }
    }
    public void buyWater(int amount) {
       try{
           shop.sellWaterToPlayer(player.getInventory(), amount);
           notificationManager.addNotification("You have bought " + amount + " water units", NotificationType.SUCCESS, farm.getCurrentDay());
        } catch (NotEnoughResourceException e)
       {
           notificationManager.addNotification("Failed to buy, not enough money", NotificationType.ERROR, farm.getCurrentDay());
       }
    }
    public void buyFertilizer(int amount) {
        try{
            shop.sellFertilizerToPlayer(player.getInventory(), amount);
            notificationManager.addNotification("You have bought " + amount + " fertilizer units", NotificationType.SUCCESS, farm.getCurrentDay());
        } catch (NotEnoughResourceException e)
        {
            notificationManager.addNotification("Failed to buy, not enough money", NotificationType.ERROR, farm.getCurrentDay());
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
                notificationManager.addNotification("You have recycled dead crop and gain " + fertilizerGained + " fertilizer unit.", NotificationType.SUCCESS, farm.getCurrentDay());
            } else {
                notificationManager.addNotification("Crop is still healthy", NotificationType.WARNING, farm.getCurrentDay());
            }
        } catch (Exception e) {
            notificationManager.addNotification(e.getMessage(), NotificationType.ERROR, farm.getCurrentDay());
        }
    }
    public void showCropStatus(Point position) {
        try {
            FarmCell cell = farm.getCell(position);

            if (cell.isEmpty()) {
                System.out.println("Cell " + position + " is empty.");
                return;
            }

            CropStatus status = cell.getCrop().getStatus();
            System.out.println("Crop status at " + position + ":");
            System.out.println(status);

        } catch (InvalidPositionException e) {
            System.out.println("Invalid position: " + position);
        }
    }

    public void displayInventory() {
    	player.getInventory().showInventory();
    }
    public void printFarmStatus() {
    	farm.printFarm();
    }
}


