package resourceManagement;

public class ResourceManager {
    private int globalWaterLevel;
    private int globalNutrientLevel;
    private static final int MIN_WATER_LEVEL = 0;
    private static final int MAX_WATER_LEVEL = 1000;
    private static final int MIN_NUTRIENT_LEVEL = 0;
    private static final int MAX_NUTRIENT_LEVEL = 1000;
    private static final int DAILY_WATER_DEPLETION = 10;
    private static final int DAILY_NUTRIENT_DEPLETION = 5;
    private static final int REPLENISH_WATER_AMOUNT = 50;
    private static final int REPLENISH_NUTRIENT_AMOUNT = 30;
    
    public ResourceManager(int initialWater, int initialNutrients) {
        this.globalWaterLevel = Math.max(MIN_WATER_LEVEL, 
                                Math.min(MAX_WATER_LEVEL, initialWater));
        this.globalNutrientLevel = Math.max(MIN_NUTRIENT_LEVEL, 
                                   Math.min(MAX_NUTRIENT_LEVEL, initialNutrients));
    }
    
    public void updateResources() {
        globalWaterLevel -= DAILY_WATER_DEPLETION;
        globalNutrientLevel -= DAILY_NUTRIENT_DEPLETION;
        globalWaterLevel = Math.max(MIN_WATER_LEVEL, 
                           Math.min(MAX_WATER_LEVEL, globalWaterLevel));
        globalNutrientLevel = Math.max(MIN_NUTRIENT_LEVEL, 
                              Math.min(MAX_NUTRIENT_LEVEL, globalNutrientLevel));
        if (globalWaterLevel < 100) {
            System.out.println("⚠️ Global water reserves are running low!");
        }
        
        if (globalNutrientLevel < 100) {
            System.out.println("⚠️ Soil nutrients are depleting!");
        }
    }
    
    public void updateResourcesWithWeather(String weatherType) {
        globalWaterLevel -= DAILY_WATER_DEPLETION;
        globalNutrientLevel -= DAILY_NUTRIENT_DEPLETION;
        if (weatherType != null) {
            switch (weatherType.toLowerCase()) {
                case "rainy":
                    globalWaterLevel += 25;
                    System.out.println("🌧️ Rain has replenished water reserves!");
                    break;
                case "drought":
                    globalWaterLevel -= 20;
                    System.out.println("☀️ Drought is depleting water faster!");
                    break;
                case "sunny":
                    break;
            }
        }
        globalWaterLevel = Math.max(MIN_WATER_LEVEL, 
                           Math.min(MAX_WATER_LEVEL, globalWaterLevel));
        globalNutrientLevel = Math.max(MIN_NUTRIENT_LEVEL, 
                              Math.min(MAX_NUTRIENT_LEVEL, globalNutrientLevel));
        if (globalWaterLevel < 100) {
            System.out.println("⚠️ Warning: Global water reserves are running low!");
        }
        
        if (globalNutrientLevel < 100) {
            System.out.println("⚠️ Warning: Soil nutrients are depleting!");
        }
    }

    public BalanceReport checkBalance(Player player) {
        if (player == null) {
            System.out.println("Error: Cannot check balance for null player");
            return null;
        }
        
        return new BalanceReport(player);
    }
    
    public boolean replenishResources() {
        if (globalWaterLevel < 50 || globalNutrientLevel < 30) {
            System.out.println("❌ Cannot replenish: Global resources too low!");
            return false;
        }
        int waterToAdd = Math.min(REPLENISH_WATER_AMOUNT, globalWaterLevel / 2);
        int nutrientToAdd = Math.min(REPLENISH_NUTRIENT_AMOUNT, globalNutrientLevel / 2);
        globalWaterLevel -= waterToAdd;
        globalNutrientLevel -= nutrientToAdd;
        
        System.out.println(String.format("✅ Resources replenished! +%d water, +%d fertilizer", 
                                        waterToAdd, nutrientToAdd));
        return true;
    }

    public int getWaterLevel() {
        return globalWaterLevel;
    }

    public int getNutrientLevel() {
        return globalNutrientLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.globalWaterLevel = Math.max(MIN_WATER_LEVEL, 
                                Math.min(MAX_WATER_LEVEL, waterLevel));
    }

    public void setNutrientLevel(int nutrientLevel) {
        this.globalNutrientLevel = Math.max(MIN_NUTRIENT_LEVEL, 
                                   Math.min(MAX_NUTRIENT_LEVEL, nutrientLevel));
    }

    public void addWater(int amount) {
        globalWaterLevel = Math.min(MAX_WATER_LEVEL, globalWaterLevel + amount);
        System.out.println("Added " + amount + " water to global reserves");
    }

    public void addNutrients(int amount) {
        globalNutrientLevel = Math.min(MAX_NUTRIENT_LEVEL, globalNutrientLevel + amount);
        System.out.println("Added " + amount + " nutrients to soil");
    }

    public boolean isCriticalState() {
        return globalWaterLevel < 100 || globalNutrientLevel < 100;
    }

    public String getResourceStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== Global Resource Status ===\n");
        status.append(String.format("Water Level: %d/%d ", globalWaterLevel, MAX_WATER_LEVEL));
        status.append(getResourceBar(globalWaterLevel, MAX_WATER_LEVEL)).append("\n");
        status.append(String.format("Nutrient Level: %d/%d ", globalNutrientLevel, MAX_NUTRIENT_LEVEL));
        status.append(getResourceBar(globalNutrientLevel, MAX_NUTRIENT_LEVEL)).append("\n");
        if (isCriticalState()) {
            status.append("⚠️ Resources running low!\n");
        }
        return status.toString();
    }

    private String getResourceBar(int current, int max) {
        int barLength = 20;
        int filled = (int) ((double) current / max * barLength);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }

    public void resetResources() {
        this.globalWaterLevel = 500;
        this.globalNutrientLevel = 500;
        System.out.println("Global resources reset to default levels");
    }
}
