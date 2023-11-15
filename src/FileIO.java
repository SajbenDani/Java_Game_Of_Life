
import java.io.*;
public class FileIO {
    private String saveDirectory;

    public FileIO(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public void saveGame(String filename, GameOfLife game) {  //játék mentése, serializációval
        try (FileOutputStream fileOut = new FileOutputStream(saveDirectory + filename + ".txt");  //txt fileba, filename néven
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(game); //a játék állapotát
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameOfLife loadGame(String filename) {  //játék betöltése
        GameOfLife loadedGame = null;
        try (FileInputStream fileIn = new FileInputStream(saveDirectory + filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            loadedGame = (GameOfLife) in.readObject(); //loadedGame-be beletesszük a file tartalmát (deserializáció)

            //A grid és a szabályok beállítása a loadedGame alapján
            if (loadedGame != null) {
                Grid loadedGrid = loadedGame.getGrid();
                GameRules loadedRules = loadedGame.getRules();

                GameOfLife newGame = new GameOfLife();
                newGame.setGrid(loadedGrid);
                newGame.setRules(loadedRules);

                loadedGame = newGame;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedGame;
    }
}
