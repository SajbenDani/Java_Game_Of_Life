import java.util.ArrayList;

public class Grid {
    private int sor; //1-tol indexel
    private int oszlop; //1-tol indexel
    //private Cell[][] grid;
    private ArrayList<Cell> grid;
    public Grid(int rows, int cols) {
        int count = rows*cols;
        sor = rows;
        oszlop = cols;
        grid = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            grid.add(new Cell(false));
        }
    }

    public ArrayList<Cell> getList() {
        return grid;
    }
    public void toggleCell(int row, int col){
        int hanyadik = row*oszlop+col;
        if(grid.get(hanyadik).isAlive())
            grid.get(hanyadik).setState(false);
        else
            grid.get(hanyadik).setState(true);
    }
    public boolean getCellValue(int row, int col){
        int hanyadik = row*oszlop+col;
        return grid.get(hanyadik).state;
    }

    public void setCellAlive(int row, int col){
        int hanyadik = row*oszlop+col;
        grid.get(hanyadik).setState(true);
    }

    public void setCellDead(int row, int col){
        int hanyadik = row*oszlop+col;
        grid.get(hanyadik).setState(false);
    }

    public ArrayList<Cell> getNeighbours(int row, int col) {  //ez fasza
        ArrayList<Cell> szomszedok = new ArrayList<>();

        // Check and add neighbors to the left and right
        if (col > 0) {
            szomszedok.add(grid.get((row * oszlop) + col - 1));
        }
        if (col < oszlop - 1) {
            szomszedok.add(grid.get((row * oszlop) + col + 1));
        }

        // Check and add neighbors above and below
        if (row > 0) {
            szomszedok.add(grid.get(((row - 1) * oszlop) + col));
            if (col > 0) {
                szomszedok.add(grid.get(((row - 1) * oszlop) + col - 1));
            }
            if (col < oszlop - 1) {
                szomszedok.add(grid.get(((row - 1) * oszlop) + col + 1));
            }
        }
        if (row < sor - 1) {
            szomszedok.add(grid.get(((row + 1) * oszlop) + col));
            if (col > 0) {
                szomszedok.add(grid.get(((row + 1) * oszlop) + col - 1));
            }
            if (col < oszlop - 1) {
                szomszedok.add(grid.get(((row + 1) * oszlop) + col + 1));
            }
        }

        return szomszedok;
    }


    public int calculateRow(int index){
        int i = index/oszlop;
        return (i);
    }
    public int calculateCol(int index){
        int i= (index%oszlop);
        return (i);
    }
    public void calculateNextGeneration(GameRules rules) {
        ArrayList<Cell> szomszedok = new ArrayList<>();
        ArrayList<Cell> nextGeneration = new ArrayList<>();

        for (int i = 0; i < grid.size(); i++) {
            System.out.println("Processing cell " + i);
            szomszedok = getNeighbours(calculateRow(i), calculateCol(i));
            int elo = 0;
            for (Cell it : szomszedok) {
                if (it.isAlive())
                    elo++;
            }
            boolean nextState = false;
            System.out.println("Alive neighbors for cell " + i + ": " + elo);
            if (!(grid.get(i).isAlive())) {
                for (int j : rules.getBirthRules()) {
                    if (elo == j) {
                        nextState = true;
                        break;
                    }
                }
                //grid.get(i).setState(temp);
            } else { //grid state true
                for (int rule : rules.getSurvivalRules()) {
                    if (elo == rule) {
                        nextState = true;
                        break;
                    }
                }
                //grid.get(i).setState(survives);
                /*if (!survives) {
                    grid.get(i).toggleState();
                }*/
            }
            nextGeneration.add(new Cell(nextState));
        }
        for (int i = 0; i < grid.size(); i++) {
            grid.get(i).setState(nextGeneration.get(i).isAlive());
        }
    }

}

