package asset;

import controller.PlayerController;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import player.Player;
import utility.CropType;

public class StoreManager {
    private AnchorPane rootPane;
    private PlayerController playerController;
    private Player player;
    private FarmRenderer renderer; // Mượn hàm loadImage
    private Runnable onPurchaseCallback; // Hàm gọi lại khi mua xong để update UI

    public StoreManager(AnchorPane rootPane, PlayerController pc, Player p, FarmRenderer fr, Runnable onPurchase) {
        this.rootPane = rootPane;
        this.playerController = pc;
        this.player = p;
        this.renderer = fr;
        this.onPurchaseCallback = onPurchase;
    }

    public void showStore() {
        if (rootPane == null) return;
        StackPane overlay = new StackPane(); overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);"); overlay.setPrefSize(rootPane.getWidth(), rootPane.getHeight());
        VBox shop = new VBox(15); shop.setAlignment(Pos.TOP_CENTER); shop.setStyle("-fx-background-color: #8B4513; -fx-background-radius: 15; -fx-border-color: #D2691E; -fx-border-width: 4; -fx-padding: 20;"); shop.setMaxSize(550, 480);

        HBox header = new HBox(15); header.setAlignment(Pos.CENTER);
        ImageView storeIcon = new ImageView(renderer.loadImage("Store")); storeIcon.setFitWidth(50); storeIcon.setFitHeight(50);
        Label title = new Label(" STORE"); title.setFont(Font.font("Arial", FontWeight.BOLD, 22)); title.setTextFill(Color.WHITE);
        HBox moneyBox = new HBox(5); moneyBox.setAlignment(Pos.CENTER); moneyBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 5; -fx-background-radius: 10;");
        ImageView moneyIcon = new ImageView(renderer.loadImage("Money")); moneyIcon.setFitWidth(20); moneyIcon.setFitHeight(20);
        Label storeMoneyLabel = new Label("$" + player.getInventory().getMoney()); storeMoneyLabel.setTextFill(Color.GOLD); storeMoneyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        moneyBox.getChildren().addAll(moneyIcon, storeMoneyLabel); header.getChildren().addAll(storeIcon, title, moneyBox);

        GridPane grid = new GridPane(); grid.setHgap(15); grid.setVgap(15); grid.setAlignment(Pos.CENTER);
        int col = 0, row = 0;

        // 1. Seeds
        for (CropType type : CropType.values()) {
            String typeName = type.toString().substring(0, 1).toUpperCase() + type.toString().substring(1).toLowerCase();
            VBox item = createShopItem(typeName, type.getSeedPrice(), typeName + "_Bag", () -> {
                playerController.buySeed(type, 1);
                storeMoneyLabel.setText("$" + player.getInventory().getMoney());
                if(onPurchaseCallback != null) onPurchaseCallback.run();
            });
            grid.add(item, col, row); col++; if (col > 2) { col = 0; row++; }
        }

        // 2. Other Items
        grid.add(createShopItem("Fertilizer", 3, "Fertilizer_Bag_Sell", () -> {
            playerController.buyFertilizer(1); storeMoneyLabel.setText("$" + player.getInventory().getMoney()); if(onPurchaseCallback != null) onPurchaseCallback.run();
        }), col, row); col++; if (col > 2) { col = 0; row++; }

        grid.add(createShopItem("Water", 2, "Water", () -> {
            playerController.buyWater(1); storeMoneyLabel.setText("$" + player.getInventory().getMoney()); if(onPurchaseCallback != null) onPurchaseCallback.run();
        }), col, row); col++; if (col > 2) { col = 0; row++; }

        grid.add(createShopItem("Medicine", 50, "Medicine", () -> {
            playerController.buyMedicine(1); storeMoneyLabel.setText("$" + player.getInventory().getMoney()); if(onPurchaseCallback != null) onPurchaseCallback.run();
        }), col, row);

        Button closeBtn = new Button("Close"); closeBtn.setStyle("-fx-background-color: #CD5C5C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> rootPane.getChildren().remove(overlay));
        shop.getChildren().addAll(header, grid, closeBtn); overlay.getChildren().add(shop); rootPane.getChildren().add(overlay);
    }

    private VBox createShopItem(String name, int price, String imageName, Runnable onBuy) {
        VBox card = new VBox(5); card.setAlignment(Pos.CENTER); card.setPrefSize(110, 140);
        card.setStyle("-fx-background-color: #F5DEB3; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);");
        ImageView img = new ImageView(renderer.loadImage(imageName)); img.setFitWidth(50); img.setFitHeight(50); img.setPreserveRatio(true);
        Label nameLabel = new Label(name); nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label priceLabel = new Label("$" + price); priceLabel.setTextFill(Color.DARKRED);
        StackPane buyBtn = new StackPane();
        ImageView btnBg = new ImageView(renderer.loadImage("Button")); btnBg.setFitWidth(70); btnBg.setFitHeight(25);
        Label btnText = new Label("BUY"); btnText.setTextFill(Color.WHITE); btnText.setFont(Font.font("System", FontWeight.BOLD, 11));
        buyBtn.getChildren().addAll(btnBg, btnText); buyBtn.setStyle("-fx-cursor: hand;");
        buyBtn.setOnMouseClicked(e -> { onBuy.run(); ScaleTransition st = new ScaleTransition(Duration.millis(100), buyBtn); st.setFromX(1.0); st.setFromY(1.0); st.setToX(0.9); st.setToY(0.9); st.setAutoReverse(true); st.setCycleCount(2); st.play(); });
        card.getChildren().addAll(img, nameLabel, priceLabel, buyBtn); return card;
    }
}