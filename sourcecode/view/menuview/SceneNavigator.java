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
    public static final String GAME_VIEW = "/view/gameview/GameMenu.fxml";

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static void loadMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(MAIN_MENU));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Smart Farm");
            stage.setScene(new Scene(root));
            
            // Cập nhật lại Stage chính của ứng dụng
            setMainStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGameScene(boolean isNewGame) {
        try {
            Farm farm = new Farm(5, 5);
            Player player = new Player();
            NotificationManager nm = new NotificationManager();
            ResourceManager shop = new ResourceManager();
            RandomEventManager randomEvent = new RandomEventManager(); // Đổi tên để tránh trùng lặp
            PlayerController pc = new PlayerController(player, farm, nm, shop, randomEvent);

            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(GAME_VIEW));
            Parent root = loader.load();

            FarmController controller = loader.getController();
            if (controller != null) {
                controller.initialize(player, farm, nm, pc); 
                if (!isNewGame) {
                    pc.loadGameCommand("smartfarm_save");
                    controller.updateGameUI(); 
                }
            }

            Stage gameStage = new Stage();
            gameStage.setTitle("Smart Farm - Game Play");
            gameStage.setScene(new Scene(root));
            gameStage.setResizable(false);

            // --- QUAN TRỌNG: XỬ LÝ DẤU X QUAY VỀ MENU ---
            gameStage.setOnCloseRequest(windowEvent -> {
                if (controller != null) {
                    // Gọi hàm confirmExitOnClose bạn đã viết trong FarmController
                    // Hàm này sẽ gọi SceneNavigator.loadMainMenu() nếu chọn YES/NO
                    controller.confirmExitOnClose(windowEvent);
                }
            });

            gameStage.show();

            if (mainStage != null) {
                mainStage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Game Scene: " + e.getMessage());
        }
    }
}