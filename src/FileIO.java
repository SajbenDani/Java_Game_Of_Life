
import javax.swing.*;
import java.io.*;
public class FileIO {
    private String saveDirectory;

    public FileIO(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }
    /**
     * játék mentése, serializációval, kivételt is kezel
     * @param filename milyen néven mentse el
     * @param game melyik gamet mentse el
     * */
    public void saveGame(String filename, GameOfLife game) {
        try (FileOutputStream fileOut = new FileOutputStream(saveDirectory + filename + ".txt");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(game); //a játék állapotát
        } catch (IOException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Hiba mentés során...");
        }
    }

    /**
     * Játék betöltése
     * LoadedGame-be beletesszük a file tartalmát
     * A grid és a szabályok beállítása a loadedGame alapján
     * Kivételt is kezelünk ha a formátum nem megfelelő
     * */
    public GameOfLife loadGame(String filename) {
        GameOfLife loadedGame = null;
        try (FileInputStream fileIn = new FileInputStream(saveDirectory + filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            loadedGame = (GameOfLife) in.readObject();


            if (loadedGame != null) {
                Grid loadedGrid = loadedGame.getGrid();
                GameRules loadedRules = loadedGame.getRules();

                GameOfLife newGame = new GameOfLife();
                newGame.setGrid(loadedGrid);
                newGame.setRules(loadedRules);

                loadedGame = newGame;
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Nem megfelelő a file formátum");
        }
        return loadedGame;
    }
}
