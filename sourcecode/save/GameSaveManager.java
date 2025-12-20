package save;
import java.io.*;
import core.GameState;
public class GameSaveManager {
    private static final String SAVE_FOLDER = "saves/";
    private static final String FILE_EXTENSION = "smartfarm_save.dat";

    public GameSaveManager() {
        File directory = new File(SAVE_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    public void saveGame(GameState state) throws IOException {
        String fullPath = SAVE_FOLDER + FILE_EXTENSION;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            oos.writeObject(state);
        }
    }
    public GameState loadGame() throws IOException, ClassNotFoundException {
        String fullPath = SAVE_FOLDER + FILE_EXTENSION;
        File file = new File(fullPath);
        if (!file.exists()) {
            throw new FileNotFoundException("No saved file found.");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullPath))) {
            return (GameState) ois.readObject();
        }
    }
}