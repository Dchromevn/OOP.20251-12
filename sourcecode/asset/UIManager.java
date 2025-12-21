package asset;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private Label dayLabel, moneyLabel, waterLabel, fertilizerLabel, boardLabel;
    private AnchorPane rootPane;
    private List<String> notificationHistory = new ArrayList<>();

    public UIManager(AnchorPane rootPane, Label day, Label money, Label water, Label fer, Label board) {
        this.rootPane = rootPane;
        this.dayLabel = day;
        this.moneyLabel = money;
        this.waterLabel = water;
        this.fertilizerLabel = fer;
        this.boardLabel = board;
    }

    public void updateHUD(int day, double money, int water, int fertilizer) {
        if (dayLabel != null) dayLabel.setText("Day " + day);
        if (moneyLabel != null) moneyLabel.setText("$" + (int)money);
        if (waterLabel != null) waterLabel.setText("Water: " + water);
        if (fertilizerLabel != null) fertilizerLabel.setText("Fer: " + fertilizer);
    }

    public void showNotification(String message, int currentDay) {
        if (boardLabel != null) {
            boardLabel.setText(message);
            FadeTransition fade = new FadeTransition(Duration.millis(200), boardLabel);
            fade.setFromValue(0.0); fade.setToValue(1.0); fade.play();

            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> boardLabel.setText(""));
            pause.play();
        }
        String logEntry = "[Day " + currentDay + "] " + message.replace("\n", " - ");
        notificationHistory.add(0, logEntry);
    }

    public void showLogBoard() {
        if (rootPane == null) return;
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());

        VBox boardBox = new VBox(15);
        boardBox.setAlignment(Pos.TOP_CENTER);
        boardBox.setMaxSize(500, 600);
        boardBox.setStyle("-fx-background-color: #8B4513; -fx-background-radius: 15; -fx-border-color: #D2691E; -fx-border-width: 4; -fx-padding: 20;");

        Label title = new Label("📜 FARM LOG");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        ListView<String> listView = new ListView<>();
        if (notificationHistory.isEmpty()) listView.getItems().add("(No notifications yet)");
        else listView.getItems().addAll(notificationHistory);

        listView.setPrefHeight(450);
        listView.setStyle("-fx-font-size: 14px; -fx-control-inner-background: #F5DEB3; -fx-background-color: #F5DEB3;");

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> rootPane.getChildren().remove(overlay));

        boardBox.getChildren().addAll(title, listView, closeBtn);
        overlay.getChildren().add(boardBox);
        rootPane.getChildren().add(overlay);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), boardBox);
        st.setFromX(0.5); st.setFromY(0.5); st.setToX(1.0); st.setToY(1.0); st.play();
    }

    public void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}