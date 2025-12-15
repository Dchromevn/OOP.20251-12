package controller;
import utility.RandomEvent;
import java.util.Random;
import core.Farm;
public class RandomEventManager {
	private final Random random = new Random();
	public RandomEvent triggerRandomEvent(Farm farm) {
		int roll = random.nextInt(100);
		if (roll < 20) {
			farm.applyRain(20);
			return RandomEvent.RAIN;
		} 
		else if (roll<35) {
			farm.applyDrought(10);
			return RandomEvent.DROUGHT;
		}
		else {
			System.out.println("A peaceful day.");
			return RandomEvent.NONE;
		}
	}
	

}
