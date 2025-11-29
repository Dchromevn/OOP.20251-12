package utility;

import core.Crop;

class CropSystemDemo {
    static void main(String[] args) {
        System.out.println("=== SMART FARM - CROP SYSTEM DEMO ===\n");

        // 1. Tạo crops bằng Factory
        System.out.println("1. Creating crops using Factory Pattern:");
        Crop wheat = CropFactory.createCrop(CropType.WHEAT, new Point(0, 0));
        Crop tomato = CropFactory.createCrop(CropType.TOMATO, new Point(1, 0));
        System.out.println("Created: " + wheat);
        System.out.println("Created: " + tomato);
        System.out.println();

        // 2. Xem crop info
        System.out.println("2. Crop Information:");
        System.out.println(CropFactory.getCropInfo(CropType.WHEAT));
        System.out.println();

        // 3. Simulate growth cycle
        System.out.println("3. Simulating Wheat Growth Cycle:");
        for (int day = 1; day <= 10; day++) {
            System.out.println("\n--- Day " + day + " ---");

            // Water and fertilize
            if (wheat.needWater()) {
                wheat.water(30);
            }
            if (wheat.needFertilizer()) {
                wheat.fertilize(20);
            }

            // Grow
            wheat.grow();

            // Check status
            CropStatus status = wheat.getStatus();
            System.out.println(status.getStatusMessage());
            System.out.println(wheat);

            // Harvest if ready
            if (wheat.isHarvestable()) {
                int money = wheat.harvest();
                System.out.println("Earned: $" + money);
                break;
            }
        }
        System.out.println("\n4. Testing Damage and Recycle:");
        Crop corn = CropFactory.createCrop(CropType.CORN, new Point(2, 0));
        corn.takeDamage(60);
        if (corn.canBeRecycled()) {
            int fertilizer = corn.recycle();
            System.out.println("Gained " + fertilizer + " fertilizer from recycling");
        }

        System.out.println("\n5. Demonstrating Polymorphism:");
        Crop[] crops = {
                CropFactory.createCrop(CropType.WHEAT, new Point(0, 0)),
                CropFactory.createCrop(CropType.CORN, new Point(1, 0)),
                CropFactory.createCrop(CropType.TOMATO, new Point(2, 0)),
                CropFactory.createCrop(CropType.CARROT, new Point(3, 0))
        };

        // Polymorphic call - same method, different behaviors
        for (Crop crop : crops) {
            crop.water(50);
            crop.fertilize(30);
        }

        System.out.println("\n=== DEMO COMPLETED ===");
    }
}