package utility;

import app.GameState;
import java.io.*;

public class GameSaveManager {
    private static final String DEFAULT_SAVE_FILE = "savegame.dat";

    public static void saveGame(GameState state, String filename) {
        String file = (filename == null || filename.isEmpty()) ? DEFAULT_SAVE_FILE : filename;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(state);
            System.out.println("Game saved successfully to " + file);
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    public static GameState loadGame(String filename) {
        String file = (filename == null || filename.isEmpty()) ? DEFAULT_SAVE_FILE : filename;
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("No save file found.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}