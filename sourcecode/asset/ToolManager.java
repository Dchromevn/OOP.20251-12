package asset;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class ToolManager {
    public enum ToolMode { NONE, WATER, FERTILIZE }

    private ToolMode currentTool = ToolMode.NONE;
    private int toolAmount = 0;

    private AnchorPane rootPane;
    private Button waterButton;
    private Button fertilizerButton;
    private UIManager uiManager; // Để gọi thông báo

    public ToolManager(AnchorPane rootPane, Button waterBtn, Button ferBtn, UIManager ui) {
        this.rootPane = rootPane;
        this.waterButton = waterBtn;
        this.fertilizerButton = ferBtn;
        this.uiManager = ui;
    }

    public void activateTool(ToolMode mode) {
        TextInputDialog dialog = new TextInputDialog(mode == ToolMode.WATER ? "10" : "5");
        dialog.setTitle("Tool Setup");
        dialog.setHeaderText(mode == ToolMode.WATER ? "💦 WATERING" : "🌿 FERTILIZER");
        dialog.setContentText("Amount:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int amount = Integer.parseInt(result.get());
                if (amount <= 0) throw new NumberFormatException();

                this.currentTool = mode;
                this.toolAmount = amount;
                rootPane.setCursor(Cursor.CROSSHAIR);

                uiManager.showNotification((mode == ToolMode.WATER ? "Water" : "Fert") + " Tool: " + amount, 0); // 0 ở đây là dummy day
                updateButtonStyles();

            } catch (NumberFormatException e) {
                uiManager.showAlert("Error", "Invalid number", Alert.AlertType.ERROR);
            }
        }
    }

    public void resetTool() {
        this.currentTool = ToolMode.NONE;
        this.toolAmount = 0;
        rootPane.setCursor(Cursor.DEFAULT);
        uiManager.showNotification("Tool deactivated", 0);
        updateButtonStyles();
    }

    private void updateButtonStyles() {
        String defaultStyle = "-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 50; -fx-border-radius: 50; -fx-cursor: hand;";
        String activeStyle = "-fx-background-color: rgba(0, 255, 0, 0.5); -fx-border-color: yellow; -fx-border-width: 3px; -fx-background-radius: 50; -fx-border-radius: 50; -fx-cursor: crosshair;";

        if (waterButton != null) waterButton.setStyle(currentTool == ToolMode.WATER ? activeStyle : defaultStyle);
        if (fertilizerButton != null) fertilizerButton.setStyle(currentTool == ToolMode.FERTILIZE ? activeStyle : defaultStyle);
    }

    public ToolMode getCurrentTool() { return currentTool; }
    public int getToolAmount() { return toolAmount; }
}