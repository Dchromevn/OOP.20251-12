package player;

import core.Inventory;
import utility.CropType;

public class Player {
    private int money;
    private int waterSupply;
    private int fertilizerSupply;
    private Inventory inventory;
    public static final int BACKPACK_CAPACITY = 100; 

    public Player(int initialMoney, int initialWater, int initialFertilizer) {
        this.money = initialMoney;
        this.waterSupply = initialWater;
        this.fertilizerSupply = initialFertilizer;
        
        // Initialize the new Inventory system
        this.inventory = new Inventory(BACKPACK_CAPACITY);
    }

    public Player() {
        this(500, 100, 50);   
    }
    public int getMoney() {
        return money;
    }

    public int getWaterSupply() {
        return waterSupply;
    }

    public int getFertilizerSupply() {
        return fertilizerSupply;
    }

    public boolean addSeed(CropType cropType, int amount) {
        return inventory.addSeed(cropType, amount);
    }

    public boolean removeSeed(CropType cropType, int amount) {
        return inventory.removeSeed(cropType, amount);
    }

    public int getSeedCount(CropType cropType) {
        return inventory.getSeedCount(cropType);
    }
    
    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }

    public void earnMoney(int amount) {
        this.money += amount;
    }
    public boolean useWater(int amount) {
        if (waterSupply >= amount) {
            waterSupply -= amount;
            return true;
        }
        return false;
    }

    public void addWater(int amount) {
        waterSupply += amount;
    }
    public boolean useFertilizer(int amount) {
        if (fertilizerSupply >= amount) {
            fertilizerSupply -= amount;
            return true;
        }
        return false;
    }

    public void addFertilizer(int amount) {
        fertilizerSupply += amount;
    }
    
    public boolean addHarvestedCrop(CropType cropType, int amount) {
        return inventory.addProduct(cropType, amount);
    }

    public int removeHarvestedCrop(CropType cropType, int amount) {
        return inventory.removeProduct(cropType, amount);
    }

    public int getHarvestedCropCount(CropType cropType) {
        return inventory.getProductCount(cropType);
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "money=" + money +
                ", waterSupply=" + waterSupply +
                ", fertilizerSupply=" + fertilizerSupply +
                '}';
    }
    public void showInventory() {
        System.out.println("=== PLAYER INVENTORY ===");
        System.out.println("Money: " + money);
        System.out.println("Water: " + waterSupply);
        System.out.println("Fertilizer: " + fertilizerSupply);

        System.out.println();
        System.out.println(inventory.toString());
    }
}
