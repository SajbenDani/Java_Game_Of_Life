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
    /**
     * Alap GameOfLife interface beállítások, illetve a játék inicializása függvénnyel.
     * */
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
        JLabel savedNameLabel = new JLabel("név:");
        JTextField savedNameText = new JTextField(10);
        JButton saveButton = new JButton("save");


        game.initializeGame(sor, oszlop);

        /**
         * Start button feladata, elindítja a számításokat, ha nem fut eleve
         * */
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running.get()) {
                    try {
                        startGame();
                    } catch (InterruptedException ex) {
                        //throw new RuntimeException(ex);
                        JOptionPane.showMessageDialog(null,"Nem sikerült a játékot elindítani");
                    }
                }

            }
        });


        /**
        * Stop button feladata, leállítja a számításokat, ha fut a játék.
        * */
        stopButton.addActionListener(new ActionListener() {  //Stop button feladata, leállítja a számításokat
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running.get()) {
                    stopGame();
                }
            }
        });

        /**
         * Save button feladata, elmenti a játék állását a textfieldben lévő néven, ha fut a játék leállítja
         * */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running.get()) {
                    stopGame();
                }
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


    /**
     * Grid inicializálása, a JPanel tipusú cellához rendelünk egy "mouse listener"-t (ezek egy-egy sejtet reprezentálnak)
     * */
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
    /**
     * segéd függvény a MenuFrameben használjuk, beállítja a "game" private változót.
     * @param game ez lesz a beállított változó értéke
     * */
    public void setGame(GameOfLife game) {
        this.game = game;
    }

    /**
     * Frissíti a tábla állapotát
     * Az élő és halott sejtek színének beállításánál, "getComponent" használata, hisz a 2 dimenziós táblát egy listaként tároljuk
     * */
    public void updateGrid() {
        for (int i = 0; i < game.getGrid().getList().size(); i++) {
            if (game.getGrid().getList().get(i).isAlive()) {
                gridPanel.getComponent(i).setBackground(Color.BLACK);
            } else {
                gridPanel.getComponent(i).setBackground(Color.WHITE);
            }
        }
    }

    /**
     * elindítja a játékot
     * beállítja a "textfield"-ben megadott paraméterek alapján a szabályokat
     * ellindítja a játékot
     * fél másodpercenként léptet egyet, és frissül a mező, itt kezelünk kivételeket is.
     * létrehozunk egy új threadet, melynek meghívjuk a start metódusát, mely elindítja a "{}" ben lévő kódot, a lambda egy "run" függvényt képvisel, ami a Runnable interface egy metódusa
     * */
    private void startGame() throws InterruptedException {
        int[] birthRules = new int[0];
        try {
            birthRules = parseRules(birthField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Hibás szabályt adott meg");
            return;
        }
        int[] survivalRules = new int[0];
        try {
            survivalRules = parseRules(aliveField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Hibás szabályt adott meg");
            return;
        }
        game.setRules(survivalRules, birthRules);
        running.set(true);

        new Thread(() -> {
            while (running.get()) {

                game.getGrid().calculateNextGeneration(game.getRules());
                updateGrid();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    /**
     * játék leállítása
     * */
    private void stopGame() {
        running.set(false);
    }

    /**
     * A textfieldből a szabályok kiolvasása, megfelelő formátumba
     * fontos, hogy max 8 szomszédunk van
     * helyes formátum ellenőrzése is megtörténik
     * @param rulesText szabályok melyet a függvényünk beolvas és beállítja a formátumot
     * @return szabályokat visszadjuk
     * */
    private int[] parseRules(String rulesText) {
        String[] ruleStrings = rulesText.split(" ");
        if (ruleStrings.length > 8) {
            //throw new IllegalArgumentException("Too many rules. Maximum is 8 rules.");
            JOptionPane.showMessageDialog(null,"túl sok szabály, max 8!");
        }

        int[] rules = new int[ruleStrings.length];


            for (int i = 0; i < ruleStrings.length; i++) {
                rules[i] = Integer.parseInt(ruleStrings[i]);
            }


        return rules;
    }
    /**
     * segéd függvény, melyet a MenuFrameben használunk
     * @param birth születési szabály, melyet berak a birthFieldbe.
     * */
    public void setBirthFieldText(String birth) {
        birthField.setText(birth);
    }
    /**
     * segéd függvény, melyet a MenuFrameben használunk
     * @param alive tulélés szabályai, melyet berak az aliveFieldbe.
     * */
    public void setAliveFieldText(String alive) {
        aliveField.setText(alive);
    }

    /**
     * belső class a mouse listenerhez (gridhez), A MouseAdapter classból örököl
     * van 2 paraméteres konstruktor, ami beállítja a helyi változókat
     * A Listenerben, a sok számolás lényege, hogy a 2 dimenziós tömb helyett, listában tároljuk a táblát, mert az könnyebben kezelhető, előre definiált függvényekkel.
     * Kattintás esetén megváltoztatja a cella állapotát és színét
     * */
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
