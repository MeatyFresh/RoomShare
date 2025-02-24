package Model_Classes;

import java.util.Date;

public class RecurringDeadline extends Deadline {
    private String recurrence;
    /**
     * Constructor for the RecurringDeadline object.
     * Takes in inputs for description and date/time the tasks should be done by.
     *
     * @param description Description of the task
     * @param by          The time the tasks should be done by.
     */
    public RecurringDeadline(String description, Date by, String recurrence) {
        super(description, by);
        this.recurrence = recurrence;
    }

    /**
     * Returns the full descriptor of the task
     * will show the type of task, whether it has been done,
     * the description of the task, the time it should be done by,
     * and the recurrence schedule of the task
     * @return A String with all the information listed above.
     */
    @Override
    public String toString() {
        return super.toString() + " (R: " + recurrence + ")";
    }
}
