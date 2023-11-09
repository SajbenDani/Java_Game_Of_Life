public class GameRules {

    private int[] survivalRules;
    private int[] birthRules;

    public GameRules() {
        // Alapértelmezett szabályok inicializálása
        survivalRules = new int[] { 2, 3 };
        birthRules = new int[] { 3 };
    }

    public void setSurvivalRules(int[] survivalRules) {
        this.survivalRules = survivalRules;
    }

    public void setBirthRules(int[] birthRules) {
        this.birthRules = birthRules;
    }

    public int[] getSurvivalRules() {
        return survivalRules;
    }

    public int[] getBirthRules() {
        return birthRules;
    }

}
