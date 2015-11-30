package heat_wave.wikileaps.utils;

/**
 * Created by heat_wave on 11/16/15.
 */
public enum Difficulty {
    EASY("Adolf Hitler"),
    MEDIUM("Herostratus"),
    HARD("André Breton");

    private String toFind;
    Difficulty(String toFind) {
        this.toFind = toFind;
    }

    @Override
    public String toString() {
        return toFind;
    }
}