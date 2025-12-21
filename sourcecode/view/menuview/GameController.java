package view.menuview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import core.GameState;
import core.Farm;
import player.Player;
import notification.NotificationManager;
import eventSystem.RandomEventManager;
import save.GameSaveManager;
import java.io.IOException;

public class GameController {

    @FXML private Label lblDay;   
    @FXML private Label lblMoney; 
    @FXML private GridPane farmGrid; // Quản lý 25 ô đất
    @FXML private AnchorPane infoPanel; // Bảng chọn cây trồng/Menu

    private GameState activeGameState;
    private final GameSaveManager saveManager = new GameSaveManager();

    // Khởi tạo giao diện sau khi FXML được load
    @FXML
    public void initialize() {
        renderFarmGrid(); // Vẽ 25 ô đất ban đầu
    }

    public void setNewGameMode(boolean isNewGame) {
        if (isNewGame) {
            createNewGame();
        } else {
            loadSavedGame();
        }
        updateUI();
    }

    private void createNewGame() {
        Farm farm = new Farm(5, 5); 
        Player player = new Player();
        NotificationManager notifManager = new NotificationManager();
        RandomEventManager eventManager = new RandomEventManager();
        this.activeGameState = new GameState(farm, player, notifManager, eventManager);
    }

    private void loadSavedGame() {
        try {
            // Tải dữ liệu từ file save đã có
            this.activeGameState = saveManager.loadGame("smartfarm_save");
        } catch (IOException | ClassNotFoundException e) {
            createNewGame(); 
        }
    }

    // Vẽ 25 ô đất Land with no crop vào GridPane
    private void renderFarmGrid() {
        if (farmGrid == null) return;
        farmGrid.getChildren().clear();
        
        String imgPath = "/data/images/Crop Status/Land with no crop.png";
        Image landImg = new Image(getClass().getResourceAsStream(imgPath));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // TẠO BIẾN TẠM THỜI Ở ĐÂY
                final int row = i; 
                final int col = j;

                ImageView cell = new ImageView(landImg);
                cell.setFitWidth(100);
                cell.setFitHeight(100);
                cell.setPreserveRatio(true);
                
                // Sử dụng biến tạm (row, col) thay vì (i, j)
                cell.setOnMouseClicked(e -> handleCellClick(row, col));
                
                farmGrid.add(cell, i, j);
            }
        }
    }
    private void handleCellClick(int x, int y) {
        System.out.println("Clicked on cell: " + x + ", " + y);
        // Logic trồng cây hoặc tưới nước sẽ thêm ở đây
    }

    @FXML
    private void handleOpenStore() {
        if (infoPanel != null) {
            infoPanel.setVisible(true); // Hiện bảng menu chọn cây
        }
    }

    @FXML
    private void handleCloseStore() {
        if (infoPanel != null) {
            infoPanel.setVisible(false); // Đóng bảng menu
        }
    }

    private void updateUI() {
        if (activeGameState == null) return;
        if (lblDay != null) lblDay.setText("Day: " + activeGameState.getFarm().getCurrentDay());
        if (lblMoney != null) lblMoney.setText("Money: $" + activeGameState.getPlayer().getInventory().getMoney());
    }
    
    @FXML
    private void onSaveGameClicked() {
        if (activeGameState != null) {
            try {
                saveManager.saveGame(activeGameState, "smartfarm_save");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}