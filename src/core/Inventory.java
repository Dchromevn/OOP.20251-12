package core;

import utility.CropType;
import java.util.*;


public class Inventory {
	private final int maxCapacity;
    private int currentLoad;
    private final Map<CropType, Integer> seeds;
    private final Map<CropType, List<Integer>> products;
    
	public Inventory(int maxCapacity) {
		this.maxCapacity = maxCapacity;
        this.currentLoad = 0;
        this.seeds = new HashMap<>();
        this.products = new HashMap<>();
        for (CropType type : CropType.values()) {
            seeds.put(type, 0);
            products.put(type, new ArrayList<>());
        }
	}
	
	public boolean hasSpace(int amount) {
        return (currentLoad + amount) <= maxCapacity;
    }
    public int getRemainingSpace() {
        return maxCapacity - currentLoad;
    }
    
    public boolean addSeed(CropType type, int amount) {
        if (!hasSpace(amount)) {
            System.out.println("❌ Inventory full! Cannot add " + amount + " seeds.");
            return false;
        }
        seeds.put(type, seeds.get(type) + amount);
        currentLoad += amount;
        return true;
    }
    public boolean removeSeed(CropType type, int amount) {
        int current = seeds.get(type);
        if (current >= amount) {
            seeds.put(type, current - amount);
            currentLoad -= amount;
            return true;
        }
        return false;
    }
    public int getSeedCount(CropType type) {
        return seeds.get(type);
    }
    
    public boolean addProduct(CropType type, int healthAdjustedValue) {
        if (!hasSpace(1)) {
            System.out.println("❌ Inventory full! Cannot harvest " + type.getCropName());
            return false;
        }
        products.get(type).add(healthAdjustedValue);
        currentLoad++;
        return true;
    }
    public int removeProduct(CropType type, int amount) {
    	List<Integer> list = products.get(type);
        int totalValue = 0;
        for (int i = 0; i < amount && !list.isEmpty(); i++) {
            totalValue += list.remove(list.size() - 1); // Remove and accumulate
            currentLoad--;
        }
        return totalValue;
    }
    public int getProductCount(CropType type) {
    	return products.get(type).size();
    }
    public int getTotalBaseValue(CropType type) {
        return products.get(type).stream().mapToInt(Integer::intValue).sum();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════╗\n");
        sb.append(String.format("║ INVENTORY (Cap: %2d/%-2d)        ║\n", currentLoad, maxCapacity));
        sb.append("╠════════════════════════════════╣\n");
        
        sb.append("║ 🌱 SEEDS:                      ║\n");
        boolean hasSeeds = false;
        for (Map.Entry<CropType, Integer> entry : seeds.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(String.format("║    %-10s x%-3d             ║\n", entry.getKey().getCropName(), entry.getValue()));
                hasSeeds = true;
            }
        }
        if (!hasSeeds) sb.append("║    (Empty)                     ║\n");

        sb.append("╠════════════════════════════════╣\n");
        
        sb.append("║ 📦 HARVESTED CROPS:            ║\n");
        boolean hasProducts = false;
        for (Map.Entry<CropType, List<Integer>> entry : products.entrySet()) {
            List<Integer> values = entry.getValue();
            if (!values.isEmpty()) {
                int totalCropValue = values.stream().mapToInt(Integer::intValue).sum();
                
                sb.append(String.format("║    %-10s x%-3d (Base Val: $%4d)        ║\n", 
                        entry.getKey().getCropName(), 
                        values.size(), 
                        totalCropValue));
                hasProducts = true;
            }
        }
        if (!hasProducts) sb.append("║    (Empty)                     ║\n");
        
        sb.append("╚════════════════════════════════╝");
        return sb.toString();
    }
}
