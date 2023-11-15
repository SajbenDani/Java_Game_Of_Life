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
        //Alap GameOfLife interface beállítások
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
        JLabel savedNameLabel = new JLabel("név:");
        JTextField savedNameText = new JTextField(10);
        JButton saveButton = new JButton("save");

        //játék inicializása
        game.initializeGame(sor, oszlop);

        startButton.addActionListener(new ActionListener() {  //Start button feladata, elindítja a számításokat
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



        stopButton.addActionListener(new ActionListener() {  //Stop button feladata, leállítja a számításokat
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running.get()) {
                    stopGame();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {  //Save button feladata, elmenti a játék állását a textfieldben lévő néven
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running.get()) {
                    FileIO fileIO = new FileIO( System.getProperty("user.dir")+ File.separator + "mentes" + File.separator);
                    fileIO.saveGame(savedNameText.getText(), game);
                }

            }
        });



        controlPanel.add(birthLabel);
        controlPanel.add(birthField);
        controlPanel.add(aliveLabel);
        controlPanel.add(aliveField);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(savedNameLabel);
        controlPanel.add(savedNameText);
        controlPanel.add(saveButton);
        add(controlPanel, BorderLayout.SOUTH);

        setSize(800, 800);
        setVisible(true);
    }

    //Grid inicializálása, a JPanel tipusú cellához rendelünk egy "mouse listener"-t (ezek egy-egy sejtet reprezentálnak)
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
    //segéd függvény a MenuFrame-hez kell
    public void setGame(GameOfLife game) {
        this.game = game;
    }

    //Frissíti a tábla állapotát
    public void updateGrid() {
        for (int i = 0; i < game.getGrid().getList().size(); i++) {
            //élő és halott sejtek színének beállítása, "getComponent" használata, hisz a 2 dimenziós táblát egy listában tároljuk
            if (game.getGrid().getList().get(i).isAlive()) {
                gridPanel.getComponent(i).setBackground(Color.BLACK);
            } else {
                gridPanel.getComponent(i).setBackground(Color.WHITE);
            }
        }
    }

    private void startGame() throws InterruptedException {  //elindítja a játékot
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
        }).start(); //létrehozunk egy új threadet, melynek meghívjuk a start metódusát, mely elindítja a "{}" ben lévő kódot, a lambda egy "run" függvényt képvisel, ami a Runnable interface egy metódusa
    }

    private void stopGame() { //játék leállítása
        running.set(false);
    }

    private int[] parseRules(String rulesText) {  //a textfieldből a szabályok kiolvasása, megfelelő formátumba
        String[] ruleStrings = rulesText.split(" ");
        if (ruleStrings.length > 8) {
            throw new IllegalArgumentException("Too many rules. Maximum is 8 rules.");  //fontos hisz max 8 szomszédunk van
        }

        int[] rules = new int[ruleStrings.length];

        try {
            for (int i = 0; i < ruleStrings.length; i++) {
                rules[i] = Integer.parseInt(ruleStrings[i]);  //helyes formátum ellenőrzése is megtörténik
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rule format. Please provide integers separated by spaces.");
        }

        return rules;
    }

    public void setBirthFieldText(String birth) {  //segéd függvény a MenuFramehez
        birthField.setText(birth);
    }

    public void setAliveFieldText(String alive) {  //segéd függvény a MenuFramehez
        aliveField.setText(alive);
    }

    private class CellMouseListener extends MouseAdapter {  //belső class a mause listenerhez (gridhez)
        private int row;
        private int col;

        public CellMouseListener(int row, int col) {  //konstruktor
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {  //Listener, a sok számolás lényege, hogy a 2 dimenziós tömb helyett, listában tároljuk a táblát, mert az könnyebben kezelhető, előre definiált függvényekkel.
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
