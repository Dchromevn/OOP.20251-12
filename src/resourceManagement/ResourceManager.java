package resourceManagement;

import player.Player;
import utility.CropType;
import core.Store;

public class ResourceManager {
	int waterPrice = Store.getWaterPrice();
    int fertilizerPrice = Store.getFertilizerPrice();
    
    public ResourceManager() {
    }

    public void sellWaterToPlayer(Player player, int amount) {
        int totalCost = amount * waterPrice;
        player.spendMoney(totalCost);
        player.gainWater(amount*25);

    }

    public void sellFertilizerToPlayer(Player player, int amount) {
        int totalCost = amount * fertilizerPrice;
        player.spendMoney(totalCost);
        player.gainFertilizer(amount*25);

    }

    public void sellSeedToPlayer(Player player,CropType type, int amount) {
        int cost = type.getSeedPrice() * amount;
        player.spendMoney(cost);
        player.addSeed(type,amount);
    }
}
