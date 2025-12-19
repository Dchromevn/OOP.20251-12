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

    public void showStoreInterface(Player player) {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║             🏘️  MARKET & STORE  🏘️               ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ WALLET: $%-12d                               ║\n", player.getMoney());
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ 1. BUY SEEDS:                                     ║");
        for (CropType type : CropType.values()) {
             System.out.printf("║  %-10s : $%3d                                 ║\n", 
                     type.getCropName(), type.getSeedPrice());
        }
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ 2. BUY RESOURCES:                                 ║");
        System.out.printf("║  💧 Water      : $%d / unit                          ║\n", WATER_PRICE);
        System.out.printf("║  🧪 Fertilizer : $%d / unit                          ║\n", FERTILIZER_PRICE);
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }
}
