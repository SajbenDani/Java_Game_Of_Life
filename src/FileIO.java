
import java.io.*;
import java.util.ArrayList;
public class FileIO {
    private String saveDirectory;// = "C:\\Users\\sajbe\\Documents\\Java_nagyhf\\Game_Of_Life\\mentes";

    public FileIO(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public void saveGame(String filename, GameOfLife game) {
        System.out.println(saveDirectory);
        try (FileOutputStream fileOut = new FileOutputStream(saveDirectory + filename + ".txt");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameOfLife loadGame(String filename) {
        GameOfLife loadedGame = null;
        try (FileInputStream fileIn = new FileInputStream(saveDirectory + filename + ".txt");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            loadedGame = (GameOfLife) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedGame;
    }
}
