package jeremy.task;

/**
 * Represents a task that starts and ends at specific dates or times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event task with a description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from Date or time when the event starts.
     * @param to Date or time when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
