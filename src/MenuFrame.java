import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuFrame extends JFrame {
    private GameOfLifeFrame gameFrame;

    public MenuFrame() {

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
            @Override
            public void actionPerformed(ActionEvent e) {
                FileIO fileIO = new FileIO(File.separator + "mentes");
                GameOfLife loadedGame = fileIO.loadGame("saved_game_name");
                if (loadedGame != null) {
                    gameFrame.dispose();
                    gameFrame = new GameOfLifeFrame(loadedGame.getGrid().getSor(), loadedGame.getGrid().getOszlop());
                    gameFrame.setGame(loadedGame);
                    gameFrame.setVisible(true);
                }
            }
        });

        gameMenu.add(newGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    public void setGameFrame(GameOfLifeFrame gameFrame) {
        this.gameFrame = gameFrame;
    }
    private void showNewGameDialog() {
        int rows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rows:"));
        int cols = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of columns:"));

        if (gameFrame != null) {
            gameFrame.dispose();
        }

        gameFrame = new GameOfLifeFrame(rows, cols);
        gameFrame.setVisible(true);
    }
}
