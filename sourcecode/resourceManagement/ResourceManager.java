package resourceManagement;

import player.Player;

public class ResourceManager {
    private int systemWaterReserve;
    private int systemFertilizerReserve;
    private static final int MAX_CAPACITY = 2000;

    public ResourceManager(int initialWater, int initialFertilizer) {
        this.systemWaterReserve = initialWater;
        this.systemFertilizerReserve = initialFertilizer;
    }

    public boolean sellWaterToPlayer(Player player, int amount, int pricePerUnit) {
        int totalCost = amount * pricePerUnit;
        if (player.getMoney() >= totalCost && systemWaterReserve >= amount) {
            player.spendMoney(totalCost);
            player.gainWater(amount);
            systemWaterReserve -= amount;
            return true;
        }
        return false;
    }

    public boolean sellFertilizerToPlayer(Player player, int amount, int pricePerUnit) {
        int totalCost = amount * pricePerUnit;
        if (player.getMoney() >= totalCost && systemFertilizerReserve >= amount) {
            player.spendMoney(totalCost);
            player.gainFertilizer(amount);
            systemFertilizerReserve -= amount;
            return true;
        }
        return false;
    }

    public void replenishSystemReserves() {
        this.systemWaterReserve = Math.min(MAX_CAPACITY, systemWaterReserve + 100);
        this.systemFertilizerReserve = Math.min(MAX_CAPACITY, systemFertilizerReserve + 50);
    }

    public String getStatus() {
        return String.format("Inventory: Water [%d/%d], Fertilizer [%d/%d]", 
                systemWaterReserve, MAX_CAPACITY, systemFertilizerReserve, MAX_CAPACITY);
    }
}