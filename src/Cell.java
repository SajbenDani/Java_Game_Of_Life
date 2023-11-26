import java.io.Serializable;

public class Cell implements Serializable {
    boolean state;
    /**
     * cella (sejt) állapota, igaz = él, hamis = halott.
     * */
    Cell(boolean state) { this.state = state; }
    /**
     * @return életben van a sejt?
     * */
    public boolean isAlive() {
        if(state == true){
            return true;
        } else{
            return false;
        }
    }
    /**
     * cella állapotának megváltoztatása az ellenkezőre
     * */
    public void toggleState() {
        if(state){
            state = false;
        } else {
            state = true;
        }
    }

    /**
     * cella állapotának beállítása
     * @param x Él e?
     * */
    public void setState(boolean x){
        state = x;
    }

}
