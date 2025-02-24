package Model_Classes;

public class RecurringToDo extends ToDo {
    private String recurrence;
    /**
     * Constructor of the RecurringTodo class. Takes in the description of the task
     *
     * @param description Description of the task.
     */
    public RecurringToDo(String description, String recurrence) {
        super(description);
        this.recurrence = recurrence;
    }

    /**
     * Returns the full descriptor of the task
     * will show the type of task, whether it has been done,
     * the description of the task,
     * and the recurrence schedule of the task
     * @return A String with all the information listed above.
     */
    @Override
    public String toString() {
        return super.toString() + " (R: " + recurrence + ")";
    }
}
