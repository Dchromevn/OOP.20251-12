package resourceManagement;

import exceptions.NotEnoughResourceException;
import player.Inventory;
import utility.CropType;

public class ResourceManager {
    private static final int WATER_PRICE = 2;
    private static final int FERTILIZER_PRICE = 5;

    public ResourceManager() {
    }

    public boolean sellWaterToPlayer(Inventory inventory, int amount) {
        int totalCost = amount * WATER_PRICE;
        if (inventory.getMoney() >= totalCost) {
            inventory.spendMoney(totalCost);
            inventory.gainWater(amount);
            return true;
        }
        return false;
    }

    public boolean sellFertilizerToPlayer(Inventory inventory, int amount) {
        int totalCost = amount * FERTILIZER_PRICE;
        if (inventory.getMoney() >= totalCost) {
            inventory.spendMoney(totalCost);
            inventory.gainFertilizer(amount);
            return true;
        }
        return false;
    }

    public boolean sellSeedToPlayer(Inventory inventory,CropType type, int amount) {
        try{
            int cost = type.getSeedPrice() * amount;
            inventory.spendMoney(cost);
            inventory.addSeed(type,amount);
            return true;
        }catch (NotEnoughResourceException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}