package app;

import java.util.Scanner;
import controller.*;
import core.*;
import resourceManagement.ResourceManager;
import eventSystem.RandomEventManager;
import player.Player;
import utility.*;
import notification.NotificationManager;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RandomEventManager eventManager = new RandomEventManager();
        ResourceManager resourceManager = new ResourceManager(1000, 1000);
        Store store = new Store();
        Player player = new Player();
        Farm farm = new Farm(5, 5);
        NotificationManager notificationManager= new NotificationManager();
        PlayerController controller = new PlayerController(player, farm);
        int waterPrice = Store.getWaterPrice();
        int fertilizerPrice = Store.getFertilizerPrice();

        System.out.println("Welcome to SmartFarm!");
        while (true) {
            System.out.println("\n===== SMART FARM MENU =====");
            System.out.println("1. Plant Crop");
            System.out.println("2. Water Crop");
            System.out.println("3. Fertilize Crop");
            System.out.println("4. Harvest Crop");
            System.out.println("5. Visit Store");
            System.out.println("6. View Inventory");
            System.out.println("7. Next Day");
            System.out.println("8. View Farm");
            System.out.println("9. View Cell");
            System.out.println("10. View Player Info");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            }

            switch (choice) {

                case 1: 
                    System.out.println("Available crops: ");
                    for (CropType t : CropType.values())
                        System.out.println("- " + t);
                    System.out.print("Enter crop type: ");
                    String typeInput = scanner.next().toUpperCase();
                    CropType type = CropType.valueOf(typeInput);
                    System.out.print("Enter X (" + farm.getWidth() + " or more for entire row Y): ");
                    int x = scanner.nextInt();
                    System.out.print("Enter Y (" + farm.getHeight() + " or more for entire column X): ");
                    int y = scanner.nextInt();
                    boolean planted = controller.plantCrop(type, new Point(x, y));
                    System.out.println(planted ? "Planted!" : "Failed to plant.");
                    break;

                case 2:
                    System.out.print("Enter X (" + farm.getWidth() + " or more for entire row Y): ");
                    x = scanner.nextInt();
                    System.out.print("Enter Y (" + farm.getHeight() + " or more for entire column X): ");
                    y = scanner.nextInt();
                    boolean watered = controller.waterCrop(new Point(x, y), 10);
                    System.out.println(watered ? "Watered!" : "Failed to water.");
                    break;

                case 3:
                    System.out.print("Enter X (" + farm.getWidth() + " or more for entire row Y): ");
                    x = scanner.nextInt();
                    System.out.print("Enter Y (" + farm.getHeight() + " or more for entire column X): ");
                    y = scanner.nextInt();
                    boolean fertilized = controller.fertilizeCrop(new Point(x, y), 5);
                    System.out.println(fertilized ? "Fertilized!" : "Failed to fertilize.");
                    break;

                case 4: 
                    System.out.print("Enter X: ");
                    x = scanner.nextInt();
                    System.out.print("Enter Y: ");
                    y = scanner.nextInt();

                    boolean harvested = controller.harvestCrop(new Point(x, y));
                    System.out.println(harvested ? "Harvested!" : "Cannot harvest.");
                    break;

                case 5: 
                	boolean inStore = true;
                    while(inStore) {
                        store.showStoreInterface(player);
                        System.out.println("\n[1] Buy Seeds  [2] Sell Crops [3] Buy Supplies  [0] Exit Store");
                        int storeChoice = scanner.nextInt();
                        
                        if (storeChoice == 0) {
                            inStore = false;
                        } else if (storeChoice == 1) {
                            // Buy Seeds
                            System.out.print("Seed Type to Buy: ");
                            String seedIn = scanner.next().toUpperCase();
                            try {
                                CropType sType = CropType.valueOf(seedIn);
                                System.out.print("Amount: ");
                                int amt = scanner.nextInt();
                                store.buySeed(player, sType, amt);
                            } catch (Exception e) { System.out.println("Invalid Input"); }
                        } else if (storeChoice == 2) {
                            // Sell Crops
                            System.out.print("Crop Type to Sell: ");
                            String sellIn = scanner.next().toUpperCase();
                            try {
                                CropType sType = CropType.valueOf(sellIn);
                                System.out.print("Amount: ");
                                int amt = scanner.nextInt();
                                store.sellProduct(player, sType, amt);
                            } catch (Exception e) { System.out.println("Invalid Input"); }
                        } else if (storeChoice == 3) {
                            // Buy Supplies
                            System.out.println("[1] Water ($" + waterPrice + ")  [2] Fertilizer ($" + fertilizerPrice + ")");
                            int supplyChoice = scanner.nextInt();
                            System.out.print("Amount: ");
                            int amount = scanner.nextInt();
                            
                            if (supplyChoice == 1) {
                                store.buyWater(player, amount);
                            } else if (supplyChoice == 2) {
                                store.buyFertilizer(player, amount);
                            } else {
                                System.out.println("Invalid input.");
                            }
                        }
                    }
                    break;
                case 6: 
                	controller.displayInventory();
                	break;

                case 7:
                    String result= farm.advanceDay(eventManager);
                    if (result != null) {
                        notificationManager.addNotification(result, NotificationType.EVENT, farm.getCurrentDay());
                    } else {
                        notificationManager.addNotification("Just a normal day", NotificationType.INFO, farm.getCurrentDay());
                    }
                    break;

                case 8: 
                    controller.printFarmStatus();
                    break;
                case 9: 
                    System.out.print("Enter X: ");
                    x = scanner.nextInt();
                    System.out.print("Enter Y: ");
                    y = scanner.nextInt();
                    Crop c = farm.getCrop(x, y);   
                    if (c != null) {
                        System.out.println(c); 
                    } else {
                        System.out.println("No crop found at (" + x + ", " + y + ")");
                    }
                    break;
                case 10: 
                	controller.printPlayerStatus();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
