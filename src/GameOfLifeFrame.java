import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameOfLifeFrame extends JFrame{
    private int sor;
    private int oszlop;
    private GameOfLife game;
    private JPanel gridPanel;
    private JTextField birthField;
    private JTextField aliveField;
    private AtomicBoolean running = new AtomicBoolean(false);
    public GameOfLifeFrame(int rows, int cols) {
        this.sor = rows;
        this.oszlop = cols;
        game = new GameOfLife();

        setTitle("Dani's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gridPanel = new JPanel(new GridLayout(rows, cols));
        initializeGrid();
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JLabel birthLabel = new JLabel("Birth:");
        JLabel aliveLabel = new JLabel("Survive:");
        birthField = new JTextField(8);  //8 szomszéd van összesen
        aliveField = new JTextField(8);
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton saveButton = new JButton("save");

        game.initializeGame(sor, oszlop); //ide ez nem kell mert benne van a start gamben

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running.get()) {
                    try {
                        startGame();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });



        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running.get()) {
                    stopGame();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running.get()) {
                    FileIO fileIO = new FileIO( System.getProperty("user.dir")+ File.separator + "mentes" + File.separator);
                    fileIO.saveGame("saved_game_name", game);
                }

            }
        });



        controlPanel.add(birthLabel);
        controlPanel.add(birthField);
        controlPanel.add(aliveLabel);
        controlPanel.add(aliveField);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(saveButton);
        add(controlPanel, BorderLayout.SOUTH);

        setSize(600, 800);
        setVisible(true);
    }

    private void initializeGrid() {
        for (int i = 0; i < sor; i++) {
            for (int j = 0; j < oszlop; j++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cell.setBackground(Color.WHITE);
                cell.addMouseListener(new CellMouseListener(i, j));
                gridPanel.add(cell);
            }
        }
    }
    public void setGame(GameOfLife game) {
        this.game = game;
    }
    private void updateGrid() {
        for (int i = 0; i < game.getGrid().getList().size(); i++) {
            if (game.getGrid().getList().get(i).isAlive()) {
                gridPanel.getComponent(i).setBackground(Color.BLACK);
            } else {
                gridPanel.getComponent(i).setBackground(Color.WHITE);
            }
        }
    }

    private void startGame() throws InterruptedException {
        int[] birthRules = parseRules(birthField.getText());
        int[] survivalRules = parseRules(aliveField.getText());
        game.setRules(survivalRules, birthRules);
        running.set(true);

        new Thread(() -> {
            while (running.get()) {
                try {
                    game.getGrid().calculateNextGeneration(game.getRules());
                    updateGrid();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /*for(int i = 0; i < game.getGrid().getList().size(); i++){
            //int row = game.getGrid().calculateRow(i);
            //int col = game.getGrid().calculateCol(i);
            if(game.getGrid().getList().get(i).isAlive()){ //row * oszlop + col
                gridPanel.getComponent(i).setBackground(Color.BLACK); //itt a számolás nem biztos hogy jo
            } else {
                gridPanel.getComponent(i).setBackground(Color.WHITE);
            }
        }*/

        //game.startGame(); //innen kiszedtuk az argumentumbol a rulest
    }

    private void stopGame() {
        running.set(false);
        //game.stopGame();
    }

    private int[] parseRules(String rulesText) {
        String[] ruleStrings = rulesText.split(" ");
        if (ruleStrings.length > 8) {
            throw new IllegalArgumentException("Too many rules. Maximum is 8 rules.");
        }

        int[] rules = new int[ruleStrings.length];

        try {
            for (int i = 0; i < ruleStrings.length; i++) {
                rules[i] = Integer.parseInt(ruleStrings[i]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rule format. Please provide integers separated by spaces.");
        }

        return rules;
    }

        private class CellMouseListener extends MouseAdapter {
        private int row;
        private int col;

        public CellMouseListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int hanyadik = row * oszlop + col;
            if (game != null) {
                game.toggleCell(row, col);
                if (game.getGrid().getList().get(hanyadik).isAlive()) {
                    gridPanel.getComponent(hanyadik).setBackground(Color.BLACK);
                } else {
                    gridPanel.getComponent(hanyadik).setBackground(Color.WHITE);
                }
            }
        }
    }
}
