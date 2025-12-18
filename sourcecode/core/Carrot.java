package core;

import utility.CropType;
import utility.Point;

public class Carrot extends Crop{
    public Carrot(String id, Point position) {
        super(id, position, CropType.CARROT);
        this.dayPerStage = new int[]{2,2,2};
        this.maxWaterLevel=100;
        this.maxFertilizerLevel= 60;
        this.waterNeedThreshold = 25;
        this.fertilizerNeedThreshold = 15;
        this.waterLevel = this.maxWaterLevel / 2;
        this.fertilizerLevel = this.maxFertilizerLevel / 2;
    }
    @Override
    protected void consumeResource() {
        waterLevel = Math.max(0, this.waterLevel - 6);
        fertilizerLevel =Math.max(0, this.fertilizerLevel -3);
    }
    @Override
    public int harvest() {
        if (!isHarvestable()) {
            System.out.println("Carrot is not ready for harvesting");
            return 0;
        }
        int value = calculateHarvestValue();
        System.out.println("Harvested Carrot: $" + value);
        return value;
    }
}