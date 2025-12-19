package resourceManagement;

import controller.PlayerController;
import player.Player;
import utility.CropType;
import java.util.Scanner;

public class Store {
    private PlayerController controller;

    public Store(PlayerController controller) {
        this.controller = controller;
    }

    public void openStore(Scanner scanner, Player player) {
        while (true) {
            System.out.println("\nFARM SHOP");
            System.out.println("Current Balance: $" + player.getInventory().getMoney());
            System.out.println("1. Buy Seeds");
            System.out.println("2. Buy Water ($2 per 25 units)");
            System.out.println("3. Buy Fertilizer ($3 per 25 units)");
            System.out.println("4. Buy Recovery Health Package ($50 per package)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select item to buy: ");

            int storeChoice = scanner.nextInt();
            if (storeChoice == 0) break;

            switch (storeChoice) {
                case 1:
                    System.out.println("\nAvailable Seeds");
                    for (CropType t : CropType.values())
                        System.out.println("- " + t + " : $" + t.getSeedPrice());

                    System.out.print("Enter crop type: ");
                    String typeInput = scanner.next().toUpperCase();
                    try {
                        CropType type = CropType.valueOf(typeInput);
                        System.out.print("Amount: ");
                        int amount = scanner.nextInt();
                        controller.buySeed(type, amount);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid crop type!");
                    }
                    break;
                case 2:
                    System.out.print("Enter water amount: ");
                    int waterAmt = scanner.nextInt();
                    controller.buyWater(waterAmt);
                    break;
                case 3:
                    System.out.print("Enter fertilizer amount: ");
                    int ferAmt = scanner.nextInt();
                    controller.buyFertilizer(ferAmt);
                    break;
                case 4:
                	System.out.print("Enter number of packages: ");
                	int medAmount = scanner.nextInt();
                	controller.buyMedicine(medAmount);
                	break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}