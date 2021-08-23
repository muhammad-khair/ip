package task;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event class.
 *
 * This class is a Task that has an 'at' datetime String.
 */
public class Event extends Task {

    public final static String SPLIT_WORD = "at";

    private final LocalDate atDate;
    
    private final LocalTime atTime;

    public Event(String description, LocalDate atDate, LocalTime atTime) {
        super(description);
        this.atDate = atDate;
        this.atTime = atTime;
    }

    /**
     * Provides a formatted String of the 'at' field.
     *
     * @return formatted String for field 'at'
     */
    public String getFormattedAt() {
        return "(" 
                + SPLIT_WORD 
                + ": " 
                + atDate 
                + ((atTime == null) ? "": " " + atTime)
                + ")";
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + ' ' + getFormattedAt();
    }
}
