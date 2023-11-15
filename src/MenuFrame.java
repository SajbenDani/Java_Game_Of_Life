import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class MenuFrame extends JFrame {
    private GameOfLifeFrame gameFrame;

    public MenuFrame() {
        //menuFrame alap design beállítások
        setTitle("Game of Life Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewGameDialog();
            }
        });

        loadGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileIO fileIO = new FileIO(System.getProperty("user.dir")+ File.separator + "mentes" + File.separator);

                //"mentes" directory A mentés mappából a "saved game files" -ok beolvasása
                File mentesDirectory = new File(System.getProperty("user.dir")+ File.separator + "mentes" + File.separator);
                File[] savedGameFiles = mentesDirectory.listFiles();

                if (savedGameFiles != null && savedGameFiles.length > 0) {
                    // Mentett állapotok kilistázása
                    String[] savedGameNames = new String[savedGameFiles.length];
                    for (int i = 0; i < savedGameFiles.length; i++) {
                        savedGameNames[i] = savedGameFiles[i].getName();
                    }
                    //Alap load menu design beállítások
                    String selectedGame = (String) JOptionPane.showInputDialog(
                            MenuFrame.this,
                            "Choose a saved game:",
                            "Load Game",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            savedGameNames,
                            savedGameNames[0]);
                    //ha létezik a választott file azt betölti
                    if (selectedGame != null) {
                        GameOfLife loadedGame = fileIO.loadGame(selectedGame);
                        if (loadedGame != null) {
                            if (gameFrame != null) {
                                gameFrame.dispose();
                            }
                            gameFrame = new GameOfLifeFrame(loadedGame.getGrid().getSor(), loadedGame.getGrid().getOszlop());
                            gameFrame.setGame(loadedGame);

                            // szabályok betétele a textfieldekbe
                            gameFrame.setBirthFieldText(konvertalo(Arrays.toString(loadedGame.getRules().getBirthRules())));
                            gameFrame.setAliveFieldText(konvertalo(Arrays.toString(loadedGame.getRules().getSurvivalRules())));

                            // A grid frissítése
                            gameFrame.updateGrid();
                            gameFrame.setVisible(true);
                        }
                    }
                } else {
                    //ha nincsen mentett file
                    JOptionPane.showMessageDialog(
                            MenuFrame.this,
                            "No saved games found.",
                            "Load Game",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        gameMenu.add(newGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    //segéd függvény, a megfelelő formátum a mentett szabályokhoz
    public String konvertalo (String input){
        String withoutBrackets = input.replaceAll("\\[|\\]", "");
        String withoutCommas = withoutBrackets.replaceAll(",\\s*", " ");
        return withoutCommas;
    }

    //inicializálás beállítása
    private void showNewGameDialog() {
        //sorok és oszlopok beállítása
        int rows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rows:"));
        int cols = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of columns:"));
        //ha esetleg már van nyitva játék és újjat nyitunk akkor azt felülírja
        if (gameFrame != null) {
            gameFrame.dispose();
        }
        //inicializálás és megjelenítés
        gameFrame = new GameOfLifeFrame(rows, cols);
        gameFrame.setVisible(true);
    }
}
