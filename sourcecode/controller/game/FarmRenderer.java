package controller.game;

import model.crops.Crop;
import model.exceptions.InvalidPositionException;
import model.core.Farm;
import model.core.FarmCell;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import utility.Point;

import java.util.HashMap;
import java.util.Map;

public class FarmRenderer {
    private Pane farmPane;
    private final Map<String, Image> imageCache = new HashMap<>();

    // Interface xử lý khi click chuột
    public interface TileClickHandler {
        void onClick(int col, int row, double screenX, double screenY);
    }

    // Cấu hình Grid

    private static final double TILE_WIDTH = 90;
    private static final double TILE_HEIGHT = 45;
    private static final double IMAGE_SIZE = 150;
    private static final double BASE_OFFSET_X = 550;
    private static final double BASE_OFFSET_Y = 170;

    private static final int GRID_SIZE = 5;

    public FarmRenderer(Pane farmPane) {
        this.farmPane = farmPane;
    }

    public void renderGrid(Farm farm, TileClickHandler onTileClick, Point selectedCell) {
        farmPane.getChildren().clear();// xóa hình ảnh
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
            	try {
	            	FarmCell cell = farm.getCell(col, row);
	                StackPane tile = createIsometricTile(row, col, cell, onTileClick);// create new for new day
	
	                double x = (col - row) * (TILE_WIDTH / 2) + BASE_OFFSET_X;
	                double y = (col + row) * (TILE_HEIGHT / 2) + BASE_OFFSET_Y;
	
	                tile.setLayoutX(x);
	                tile.setLayoutY(y);
	
	                if (selectedCell != null && selectedCell.getX() == col && selectedCell.getY() == row) {
	                    if (tile.getChildren().size() > 1) {
	                        tile.getChildren().get(1).setEffect(new DropShadow(20, Color.YELLOW));
	                    }
	                }
            	
            	farmPane.getChildren().add(tile);
            	} catch (InvalidPositionException e) {
            	System.err.println("Rendering error at [" + col + "," + row + "]: " + e.getMessage());
            	}
            }
     	}
    }
    // tạo ra hình ảnh các crop
    private StackPane createIsometricTile(int row, int col, FarmCell cell, TileClickHandler onTileClick) {
        StackPane tile = new StackPane();
        tile.setPrefSize(TILE_WIDTH, TILE_HEIGHT * 2);
        tile.setPickOnBounds(false);

        Polygon hitbox = new Polygon(TILE_WIDTH / 2.0, 0.0, TILE_WIDTH, TILE_HEIGHT / 2.0, TILE_WIDTH / 2.0, TILE_HEIGHT, 0.0, TILE_HEIGHT / 2.0);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.TRANSPARENT);

        ImageView visual = new ImageView();
        visual.setFitWidth(IMAGE_SIZE);
        visual.setFitHeight(IMAGE_SIZE);
        visual.setPreserveRatio(true);
        visual.setMouseTransparent(true);

        if (cell.isEmpty()) visual.setImage(loadImage("Land"));
        else visual.setImage(loadImage(getCropImageName(cell.getCrop())));// hm gọi ra hình ảnh các crop khi mà thay đổi stage

        tile.getChildren().addAll(hitbox, visual);
        if (!cell.isEmpty()) addCropOverlays(tile, cell.getCrop());
        // làm sáng ô đất để người dùng dễ pht hiện
        ColorAdjust br = new ColorAdjust(); br.setBrightness(0.3);
        hitbox.setOnMouseEntered(e -> visual.setEffect(br));
        hitbox.setOnMouseExited(e -> visual.setEffect(null));

        // Truyền tọa độ khi click
        hitbox.setOnMouseClicked(e -> onTileClick.onClick(col, row, e.getScreenX(), e.getScreenY()));

        return tile;
    }

    private void addCropOverlays(StackPane tile, Crop crop) {
        if (crop.isDead()) return;
        VBox overlay = new VBox(2);
        overlay.setAlignment(Pos.CENTER);
        overlay.setMouseTransparent(true);
        overlay.setTranslateY(-110);

        tile.getChildren().add(overlay);
    }
    // method để lấy tên ảnh
    private String getCropImageName(Crop crop) {
        String typeStr = crop.getCropType().toString();
        String typeName = typeStr.substring(0, 1).toUpperCase() + typeStr.substring(1).toLowerCase();
        String suffix = "Mature";
        if (crop.isDead()) suffix = "Dead";
        else if (crop.isHarvestable()) {
            suffix = "Harvestable";
        } else {
            String stage = crop.getCurrentStage().toString();
            if (stage.equals("SEED")) suffix = "Seed";
            else if (stage.equals("SEEDLING")) suffix = "Seedling";
        }
        return typeName + "_" + suffix;
    }
    // Load images từ data
    public Image loadImage(String fileName) {
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
        if (imageCache.containsKey(baseName)) return imageCache.get(baseName);
        String[] extensions = {".png", ".jpg", ".jpeg", ".PNG", ".JPG", ".JPEG"};
        Image img = null;
        for (String ext : extensions) {
            String path = "/view/images/" + baseName + ext;
            try {
                java.io.InputStream stream = getClass().getResourceAsStream(path);
                if (stream == null) {
                    path = "/images/" + baseName + ext;
                    stream = getClass().getResourceAsStream(path);
                }
                if (stream != null) {
                    img = new Image(stream);
                    if (!img.isError()) break;
                }
            } catch (Exception e) {}
        }
        if (img == null || img.isError()) {
            if (!baseName.equals("Land")) return loadImage("Land");
            return null;
        }
        imageCache.put(baseName, img);
        return img;
    }
}