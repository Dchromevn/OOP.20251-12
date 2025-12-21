package view.menuview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.gameview.FarmController;
import core.Farm;
import player.Player;
import controller.PlayerController;
import notification.NotificationManager;
import resourceManagement.ResourceManager;
import eventSystem.RandomEventManager;
import java.io.IOException;

public class SceneNavigator {
    public static final String MAIN_MENU = "/view/menuview/MainMenu.fxml";
    // Make sure this path matches your actual game fxml file name
    public static final String GAME_VIEW = "/view/gameview/GameMenu.fxml";

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }


    // --- 3. SPECIFIC GAME NAVIGATOR (For Start/Continue) ---
    // This fixes the error in MainMenuController
    public static void loadGameScene(boolean isNewGame) {
        try {
            Farm farm = new Farm(5, 5);
            Player player = new Player();
            NotificationManager nm = new NotificationManager();
            ResourceManager shop = new ResourceManager();
            RandomEventManager event = new RandomEventManager();
            PlayerController pc = new PlayerController(player, farm, nm, shop, event);
            // 1. Tải file FXML cho màn hình Game
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(GAME_VIEW));
            Parent root = loader.load();

            // 2. Thiết lập chế độ chơi cho GameController
            FarmController controller = loader.getController();
            if (controller != null) {
                controller.initialize(player,farm,nm,pc); 
                if (!isNewGame) {
                    pc.loadGameCommand("smartfarm_save");
                    // Refresh UI after loading
                    controller.updateGameUI(); 
                }
            }

            // 3. TẠO CỬA SỔ MỚI (STAGE MỚI)
            Stage gameStage = new Stage();
            gameStage.setTitle("Smart Farm - Game Play");
            gameStage.setScene(new Scene(root));
            gameStage.setResizable(false);
            // 4. Hiển thị cửa sổ mới
            gameStage.show();

            // 5. (Tùy chọn) Đóng cửa sổ Menu chính nếu bạn muốn
             if (mainStage != null) {
                mainStage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Game Scene: " + e.getMessage());
        }
    }
}