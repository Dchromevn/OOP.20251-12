package view.menuview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    // --- 1. DEFINE YOUR VIEW PATHS ---
    public static final String MAIN_MENU = "/menuview/GameMenu.fxml";
    // Make sure this path matches your actual game fxml file name
    public static final String GAME_VIEW = "/menuview/GameView.fxml";

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }


    // --- 3. SPECIFIC GAME NAVIGATOR (For Start/Continue) ---
    // This fixes the error in MainMenuController
    public static void loadGameScene(boolean isNewGame) {
        try {
            // 1. Tải file FXML cho màn hình Game
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(GAME_VIEW));
            Parent root = loader.load();

            // 2. Thiết lập chế độ chơi cho GameController
            GameController controller = loader.getController();
            if (controller != null) {
                controller.setNewGameMode(isNewGame); 
            }

            // 3. TẠO CỬA SỔ MỚI (STAGE MỚI)
            Stage gameStage = new Stage();
            gameStage.setTitle("Smart Farm - Game Play");
            gameStage.setScene(new Scene(root));
            
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