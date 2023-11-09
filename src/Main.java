import javax.swing.*;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*SwingUtilities.invokeLater(() -> {
            GameOfLifeFrame game = new GameOfLifeFrame(100, 100);
        });*/
        //GameOfLifeFrame game = new GameOfLifeFrame(4, 4);
        /*GameOfLife game = new GameOfLife();
        game.initializeGame(4,4);
        int[] a = new int[1];
        int[] b = new int[2];
        a[0] = 3;
        b[0] = 2;
        b[1] = 3;
        game.setRules(a, b);
        game.getGrid().getList().get(9).setState(true);
        //game.getGrid().getList().get(5).setState(true);
        //game.getGrid().getList().get(8).setState(true);
        //game.getGrid().getList().get(10).setState(true);
        ArrayList<Cell> szomszedok = new ArrayList<>();
        szomszedok = game.getGrid().getNeighbours(2,1);
        game.getGrid().calculateNextGeneration(game.getRules());
        //System.out.println(szomszedok);
        //System.out.println(game.getGrid().getList().get(8));
        //System.out.println();
        //game.startGame();*/
        //GameOfLifeFrame game = new GameOfLifeFrame(100, 100);
        MenuFrame menuFrame = new MenuFrame();
        menuFrame.setVisible(true);
        //todo: serializácios mentes, egy menu amibol belehet tolteni elozzo beallitast vagy uj jatekot kezdeni, illetve valami settings ful ahol meg lehet adni a paraméter hogy mekkora legyen a grid



    }
}