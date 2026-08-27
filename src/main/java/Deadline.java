package jeremy.task;

/**
 * Represents a task that needs to be completed before a specific date or time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline task with the specified description and due date or time.
     *
     * @param description Description of the task.
     * @param by Date or time by which the task should be completed.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
