package core;

import player.Player;
import utility.CropType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Store {
    private final Map<CropType, Double> priceMultipliers;
    private final Random random;
    
    private static final int WATER_PRICE = 1;
	private static final int FERTILIZER_PRICE = 2;
    public Store() {
        this.random = new Random();
        this.priceMultipliers = new HashMap<>();

        for (CropType type : CropType.values()) {
            priceMultipliers.put(type, 1.0);
        }
    }
    
    public static int getWaterPrice() {
		return WATER_PRICE;
	}

	public static int getFertilizerPrice() {
		return FERTILIZER_PRICE;
	}

	public void updateMarketPrices() {
	    for (CropType type : CropType.values()) {
	        double fluctuation = 0.8 + (random.nextDouble() * 0.5); 

	        if (random.nextDouble() < 0.05) {
	            fluctuation += 0.5;
	        }

	        priceMultipliers.put(type, fluctuation);
	    }
	}

    public int getSellPrice(CropType type, int healthAdjustedBase) {
        double multiplier = priceMultipliers.getOrDefault(type, 1.0);
        return (int) (healthAdjustedBase * multiplier);
    }
    public int getSeedCost(CropType type) {
        return type.getSeedPrice();
    }
    public boolean buySeed(Player player, CropType type, int amount) {
        int totalCost = getSeedCost(type) * amount;
        if (!player.getInventory().hasSpace(amount)) {
            System.out.println("❌ Transaction Failed: Not enough inventory space!");
            return false;
        }

        if (player.getMoney() < totalCost) {
            System.out.println("❌ Transaction Failed: Not enough money! Need $" + totalCost);
            return false;
        }

        player.spendMoney(totalCost);
        player.addSeed(type, amount);
        System.out.println("✅ Purchased " + amount + " " + type.getCropName() + " seeds for $" + totalCost);
        return true;
    }
    
    public boolean buyWater(Player player, int amount) {
        int totalCost = WATER_PRICE * amount;

        if (player.getMoney() < totalCost) {
            System.out.println("❌ Not enough money to buy water! Need $" + totalCost);
            return false;
        }

        player.spendMoney(totalCost);
        player.addWater(amount);
        System.out.println("💧 Purchased " + amount + "L Water for $" + totalCost);
        return true;
    }

    public boolean buyFertilizer(Player player, int amount) {
        int totalCost = FERTILIZER_PRICE * amount;

        if (player.getMoney() < totalCost) {
            System.out.println("❌ Not enough money to buy fertilizer! Need $" + totalCost);
            return false;
        }

        player.spendMoney(totalCost);
        player.addFertilizer(amount);
        System.out.println("🧪 Purchased " + amount + "kg Fertilizer for $" + totalCost);
        return true;
    }

    public boolean sellProduct(Player player, CropType type, int amount) {
        int currentStock = player.getHarvestedCropCount(type);
        if (currentStock < amount) {
            System.out.println("❌ Transaction Failed: You don't have enough " + type.getCropName());
            return false;
        }
        int combinedBaseValue = player.removeHarvestedCrop(type, amount);
        double multiplier = priceMultipliers.get(type);
        int totalEarnings = (int) (combinedBaseValue * multiplier);
        player.earnMoney(totalEarnings);
        System.out.println("✅ Sold for $" + totalEarnings + " (Market Factor: " + String.format("%.2f", multiplier) + "x)");
        return true;
    }

    public void showStoreInterface(Player player) {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║             🏘️  MARKET & STORE  🏘️               ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ WALLET: $%-12d  CAPACITY: %2d/%-2d slots  ║\n", 
                player.getMoney(), 
                (player.getInventory().getRemainingSpace() == Player.BACKPACK_CAPACITY ? 0 : Player.BACKPACK_CAPACITY - player.getInventory().getRemainingSpace()), 
                Player.BACKPACK_CAPACITY);
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ 1. SELL INVENTORY (Current Market Trends):        ║");
        
        for (CropType type : CropType.values()) {
            double multiplier = priceMultipliers.getOrDefault(type, 1.0);
            int count = player.getHarvestedCropCount(type);

            int totalBaseValue = player.getInventory().getTotalBaseValue(type);
            int potentialEarnings = getSellPrice(type, totalBaseValue);
            
            String trend = getTrendIndicator(multiplier);

            System.out.printf("║  %-10s : x%-2d -> Value: $%4d  %-11s ║\n", 
                    type.getCropName(), count, potentialEarnings, trend);
        }
        
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ 2. BUY SEEDS:                                     ║");
        for (CropType type : CropType.values()) {
             System.out.printf("║  %-10s : $%3d                                 ║\n", 
                     type.getCropName(), type.getSeedPrice());
        }
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ 3. BUY RESOURCES:                                 ║");
        System.out.printf("║  💧 Water      : $%d / unit                          ║\n", WATER_PRICE);
        System.out.printf("║  🧪 Fertilizer : $%d / unit                          ║\n", FERTILIZER_PRICE);
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    private String getTrendIndicator(double multiplier) {
    	if (multiplier > 1.1) return "🔺 (High)";
        if (multiplier < 0.9) return "🔻 (Low)";
        return "   (Stable)";
    }
}
