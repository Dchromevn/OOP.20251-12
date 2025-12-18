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
            double fluctuation = 0.5 + (random.nextDouble() * 1.0);
            priceMultipliers.put(type, fluctuation);
        }
    }

    public int getSellPrice(CropType type) {
        int basePrice = type.getBasePriceCrop();
        double multiplier = priceMultipliers.get(type);
        return (int) (basePrice * multiplier);
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
        // 1. Check if player has the items to sell
        int currentStock = player.getHarvestedCropCount(type);
        if (currentStock < amount) {
            System.out.println("❌ Transaction Failed: You don't have enough " + type.getCropName());
            return false;
        }

        // 2. Calculate earnings based on DYNAMIC prices
        int unitPrice = getSellPrice(type);
        int totalEarnings = unitPrice * amount;

        // 3. Execute Transaction
        player.removeHarvestedCrop(type, amount);
        player.earnMoney(totalEarnings);
        System.out.println("✅ Sold " + amount + " " + type.getCropName() + " for $" + totalEarnings + " (@ $" + unitPrice + "/unit)");
        return true;
    }

    public void showStoreInterface(Player player) {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║            🏘️  STORE  🏘️              ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.printf("║ WALLET: $%-32d ║\n", player.getMoney());
        System.out.printf("║ BACKPACK:    %2d/%-2d slots used                  ║\n", 
                (Player.BACKPACK_CAPACITY - player.getInventory().getRemainingSpace()), Player.BACKPACK_CAPACITY );
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║ 1. SELL CROPS (Current Market Prices):        ║");
        for (CropType type : CropType.values()) {
            int base = type.getBasePriceCrop();
            int current = getSellPrice(type);
            String trend = getTrendIndicator(base, current);
            System.out.printf("║  %-10s : $%3d %-20s ║\n", type.getCropName(), current, trend);
        }
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║ 2. BUY SEEDS:                                 ║");
        for (CropType type : CropType.values()) {
             System.out.printf("║  %-10s : $%3d                             ║\n", type.getCropName(), type.getSeedPrice());
        }
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("║ 3. BUY RESOURCES:                             ║");
        System.out.printf("║  💧 Water      : $%d / unit                      ║\n", WATER_PRICE);
        System.out.printf("║  🧪 Fertilizer : $%d / unit                      ║\n", FERTILIZER_PRICE);
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    private String getTrendIndicator(int base, int current) {
        if (current > base) return "🔺 (Up)";
        if (current < base) return "🔻 (Down)";
        return "   (Normal)";
    }
}
