package jeremy.task;

/** A task without any date/time attached, e.g. "visit new theme park". */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
        assert getTypeIcon().equals("T") : "Todo must use the T type icon";
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }
}
