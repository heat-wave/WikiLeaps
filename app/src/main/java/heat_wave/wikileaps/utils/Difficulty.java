package heat_wave.wikileaps.utils;

/**
 * Created by heat_wave on 11/16/15.
 */
public enum Difficulty {
    EASY("Łódź"),
    MEDIUM("Coca-Cola"),
    HARD("Euclidis");

    private String toFind;
    Difficulty(String toFind) {
        this.toFind = toFind;
    }

    @Override
    public String toString() {
        return toFind.replace(' ', '_');
    }
}