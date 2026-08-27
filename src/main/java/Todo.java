package jeremy.task;

/**
 * Represents a task without a date or time attached.
 */
public class Todo extends Task {
    /**
     * Creates a todo task with the specified description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }
}
