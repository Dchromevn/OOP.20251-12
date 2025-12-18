package eventSystem;

import java.util.Random;

public class RandomEventManager {
    private final Random random = new Random();

    public GameEvent triggerRandomEvent() {
        int roll = random.nextInt(100);
        if (roll < 35) {
            return new Rain();
        } else if (roll < 55) {
            return new Drought();
        } else if (roll < 65) {
            return new PestAttack();
        } else {
            return null;
        }
    }
}

