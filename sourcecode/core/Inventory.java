package core;

import utility.CropType;
import java.util.*;


public class Inventory {
	private final int maxCapacity;
    private int currentLoad;
    private final Map<CropType, Integer> seeds;
    
	public Inventory(int maxCapacity) {
		this.maxCapacity = maxCapacity;
        this.currentLoad = 0;
        this.seeds = new HashMap<>();
        for (CropType type : CropType.values()) {
            seeds.put(type, 0);
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════╗\n");
        sb.append(String.format("║ INVENTORY (Seeds: %2d/%-2d)      ║\n", currentLoad, maxCapacity));
        sb.append("╠════════════════════════════════╣\n");
        
        boolean hasSeeds = false;
        for (Map.Entry<CropType, Integer> entry : seeds.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(String.format("║    %-10s x%-3d             ║\n", entry.getKey().getCropName(), entry.getValue()));
                hasSeeds = true;
            }
        }
        if (!hasSeeds) sb.append("║    (No Seeds)                  ║\n");
        sb.append("╚════════════════════════════════╝");
        return sb.toString();
    }
}
