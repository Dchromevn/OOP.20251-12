package core;

import utility.CropType;
import utility.Point;

public class Corn extends Crop{
	public Corn(String id, Point position) {
        super(id, position, CropType.CORN);
        this.dayPerStage = new int[]{1,2,2};
        this.maxWaterLevel=80;
        this.maxFertilizerLevel= 50;
        this.waterNeedThreshold = 20;
        this.fertilizerNeedThreshold = 15;
        this.waterLevel = this.maxWaterLevel / 2;
        this.fertilizerLevel = this.maxFertilizerLevel / 2;
    }
    @Override
    protected void consumeResource() {
        waterLevel = Math.max(0, this.waterLevel - 5);
        fertilizerLevel =Math.max(0, this.fertilizerLevel -3);
    }
}