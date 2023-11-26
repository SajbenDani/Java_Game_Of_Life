import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class MenuFrame extends JFrame {
    private GameOfLifeFrame gameFrame;
    /**
     * menuFrame alap design beállítások
     *
     * */
    public MenuFrame() {

        setTitle("Game of Life Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        /**
         * A JMenuItem-hez csinálunk egy action listenert
         * */
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewGameDialog();
            }
        });
        /**
         * ActionListener a LoadGameItem-hez
         * "mentesDirectory" A mentés mappából a "savedGameFiles" -ok beolvasása
         * A Mentett állapotok kilistázása
         * Alap load menu design beállítása
         * Ha létezik a választott file azt betölti
         * Szabályok betétele a textfieldekbe
         * A grid frissítése
         * van külön eset, hogyha nincsen mentett file
         * */
        loadGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    FileIO fileIO = new FileIO(System.getProperty("user.dir") + File.separator + "mentes" + File.separator);


                    File mentesDirectory = new File(System.getProperty("user.dir") + File.separator + "mentes" + File.separator);
                    File[] savedGameFiles = mentesDirectory.listFiles();

                    if (savedGameFiles != null && savedGameFiles.length > 0) {

                        String[] savedGameNames = new String[savedGameFiles.length];
                        for (int i = 0; i < savedGameFiles.length; i++) {
                            savedGameNames[i] = savedGameFiles[i].getName();
                        }

                        String selectedGame = (String) JOptionPane.showInputDialog(
                                MenuFrame.this,
                                "Choose a saved game:",
                                "Load Game",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                savedGameNames,
                                savedGameNames[0]);

                        if (selectedGame != null) {
                            GameOfLife loadedGame = fileIO.loadGame(selectedGame);
                            if (loadedGame != null) {
                                if (gameFrame != null) {
                                    gameFrame.dispose();
                                }
                                gameFrame = new GameOfLifeFrame(loadedGame.getGrid().getSor(), loadedGame.getGrid().getOszlop());
                                gameFrame.setGame(loadedGame);


                                gameFrame.setBirthFieldText(konvertalo(Arrays.toString(loadedGame.getRules().getBirthRules())));
                                gameFrame.setAliveFieldText(konvertalo(Arrays.toString(loadedGame.getRules().getSurvivalRules())));


                                gameFrame.updateGrid();
                                gameFrame.setVisible(true);
                            }
                        }
                    } else {

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

    /**
     * segéd függvény, a megfelelő formátumhoz, a mentett szabályok konvertálása.
     * */
    public String konvertalo (String input){
        String withoutBrackets = input.replaceAll("\\[|\\]", "");
        String withoutCommas = withoutBrackets.replaceAll(",\\s*", " ");
        return withoutCommas;
    }


    /**
     * inicializálás, sorok és oszlopok beállítása
     * ha esetleg már van nyitva játék és újjat nyitunk akkor azt felülírja
     * végül megjelenítés
     * */
    private void showNewGameDialog() {

        int cols = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of the table:"));
        int rows = cols;

        if (gameFrame != null) {
            gameFrame.dispose();
        }

        gameFrame = new GameOfLifeFrame(rows, cols);
        gameFrame.setVisible(true);
    }
}
