package utility;

public enum CropType {
    WHEAT("Wheat",4, 15),
    CORN("Corn",10,30),
    CARROT("Carrot",15,50),
    TOMATO("Tomato",20,75),
    PUMPKIN("Pumpkin",30,160);
    private final String cropName;
    private final int seedPrice;
    private final int basePriceCrop;
    CropType(String cropName,int seedPrice,int basePriceCrop){
        this.cropName=cropName;
        this.seedPrice=seedPrice;
        this.basePriceCrop=basePriceCrop;
    }
    public String getCropName(){
        return cropName;
    }
    public int getSeedPrice(){
        return seedPrice;
    }
    public int getBasePriceCrop(){
        return basePriceCrop;
    }
}
