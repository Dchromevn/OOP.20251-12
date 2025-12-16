package controller;
import core.Farm;
import core.FarmCell;
import core.Crop;
import eventSystem.RandomEventManager;
import player.Player;
import utility.CropType;
import utility.Point;
import java.util.ArrayList;
import java.util.List;

public class PlayerController {

    private Player player;
    private Farm farm;

    public PlayerController(Player player, Farm farm) {
        this.player = player;
        this.farm = farm;
    }
    
    private List<Point> getTargetPoints(Point inputPos) {
        List<Point> targets = new ArrayList<>();
        int w = farm.getWidth();  
        int h = farm.getHeight(); 
        int x = inputPos.getX();
        int y = inputPos.getY();

        // Check if input is a "Magic Number" (>= Width/Height)
        if (x >= w && y >= h) { // ALL FARM
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    targets.add(new Point(i, j));
                }
            }
        } else if (x >= w) { // ENTIRE ROW y
            if (y >= 0 && y < h) {
                for (int i = 0; i < w; i++) {
                    targets.add(new Point(i, y));
                }
            }
        } else if (y >= h) { // ENTIRE COLUMN x
            if (x >= 0 && x < w) {
                for (int j = 0; j < h; j++) {
                    targets.add(new Point(x, j));
                }
            }
        } else { // SINGLE CELL
            if (farm.isValidPosition(inputPos)) {
                targets.add(inputPos);
            }
        }
        return targets;
    }

    public boolean plantCrop(CropType type, Point position) {
        List<Point> targets = getTargetPoints(position);
        int count = 0;
        for (Point p : targets) {
            if (player.getSeedCount(type) <= 0) {
                System.out.println("⚠️ Ran out of seeds! Stopped at " + p);
                break;
            }
            FarmCell cell = farm.getCell(p);
            if (cell == null || !cell.isEmpty()) continue; 
            Crop crop = utility.CropFactory.createCrop(type, p);
            cell.plantCrop(crop);
            player.removeSeed(type, 1);
            count++;
        }
        
        return count > 0;
    }

    public boolean waterCrop(Point position, int amount) {
        List<Point> targets = getTargetPoints(position);
        int count = 0;
        for (Point p : targets) {
            FarmCell cell = farm.getCell(p);
            if (cell == null || cell.isEmpty()) continue;
            if (!player.useWater(amount)) {
                 System.out.println("⚠️ Ran out of water! Stopped at " + p);
                 break;
            }
            cell.getCrop().water(amount);
            count++;
        }
        return count > 0;
    }
    
    public boolean fertilizeCrop(Point position, int amount) {
        List<Point> targets = getTargetPoints(position);
        int count = 0;
        for (Point p : targets) {
            FarmCell cell = farm.getCell(p);
            if (cell == null || cell.isEmpty()) continue;
            if (!player.useFertilizer(amount)) {
                System.out.println("⚠️ Ran out of fertilizer! Stopped at " + p);
                break; 
            }
            cell.getCrop().fertilize(amount);
            count++;
        }
        return count > 0;
    }
    public boolean harvestCrop(Point position) {
    	FarmCell cell = farm.getCell(position);
    	if (cell == null || cell.isEmpty()) return false;
    	Crop crop = cell.getCrop();
    	if (!crop.isHarvestable()) return false;
    	int yield = crop.harvest();
    	if (!player.getInventory().hasSpace(yield)) return false;
    	crop.harvest();
    	CropType type = crop.getCropType();
        player.addHarvestedCrop(type, yield);
    	cell.removeCrop();
    	return true;
    }
    public void nextDay(RandomEventManager eventManager) {
        farm.advanceDay(eventManager);
    }
    public void displayInventory() {
    	player.showInventory();
    }
    public void printPlayerStatus() {
    	System.out.println(player);
    }
    public void printFarmStatus() {
    	farm.printFarm();
    }
}
