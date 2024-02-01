package idatt2106.group3.backend.Enum;

/**
 * Enumerator values for difficulty, as for user's trainingLevel and difficulty of an activity.
 * We use numeric values of enums to be able to have mutliple difficulty levels on an activity
 * without the need to make an extra static database table.
 */
public enum Difficulty
{
    EASY(1),
    MEDIUM(2),
    HARD(4);

    public final int value;

    Difficulty(int value) {
        this.value = value;
    }
}
