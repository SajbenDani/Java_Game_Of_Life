import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {
    private JPanel panel;
    private GameOfLifeFrame gameFrame;

    public MenuFrame() {
        /*setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel(new FlowLayout());
        add(panel, BorderLayout.CENTER);
        JButton newGame = new JButton("New Game");
        JButton load = new JButton("Load Game");

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {

                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });*/
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
                // Implement the load game functionality here
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
