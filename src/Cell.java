public class Cell {
    boolean state;
    Cell(boolean state) { this.state = state; }
    public boolean isAlive() {
        if(state == true){
            return true;
        } else{
            return false;
        }
    }
    public void toggleState() {
        if(state){
            state = false;
        } else {
            state = true;
        }
    }

    public boolean getState() {
        return state;
    }
    public void setState(boolean x){
        state = x;
    }

}
