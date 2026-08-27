package jeremy.parser;

import jeremy.exception.JeremyException;
import jeremy.task.Deadline;
import jeremy.task.Event;

/**
 * Parses raw user commands into command words, arguments, and task objects.
 */
public class Parser {

    /**
     * Returns the first token of the input command in lower case.
     *
     * @param fullCommand Complete user command.
     * @return Lower-case command word.
     */
    public String getCommandWord(String fullCommand) {
        String trimmed = fullCommand.trim();
        int spaceIndex = trimmed.indexOf(' ');
        String word = spaceIndex >= 0 ? trimmed.substring(0, spaceIndex) : trimmed;
        return word.toLowerCase();
    }

    /**
     * Returns the text after the first command token, with surrounding whitespace removed.
     *
     * @param fullCommand Complete user command.
     * @return Command arguments, or an empty string when no arguments are present.
     */
    public String getArguments(String fullCommand) {
        String trimmed = fullCommand.trim();
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex >= 0 ? trimmed.substring(spaceIndex + 1).trim() : "";
    }

    /**
     * Parses a numeric task index for mark, unmark, or delete operations.
     *
     * @param args Task index supplied by the user.
     * @param actionWord Action associated with the task index.
     * @return Parsed task index.
     * @throws JeremyException If the argument is empty or is not a valid integer.
     */
    public int parseIndex(String args, String actionWord) throws JeremyException {
        if (args.isEmpty()) {
            throw new JeremyException("Which task? Use: " + actionWord + " <task number>.");
        }
        try {
            return Integer.parseInt(args);
        } catch (NumberFormatException exception) {
            throw new JeremyException("'" + args + "' isn't a valid task number.");
        }
    }

    /**
     * Returns a validated todo description from the supplied arguments.
     *
     * @param args Raw arguments following the todo command.
     * @return Todo description.
     * @throws JeremyException If the description is empty.
     */
    public String parseTodoDescription(String args) throws JeremyException {
        if (args.isEmpty()) {
            throw new JeremyException("The description of a todo cannot be empty.");
        }
        return args;
    }

    /**
     * Returns a deadline parsed from the supplied command arguments.
     *
     * @param args Raw arguments following the deadline command.
     * @return Parsed deadline task.
     * @throws JeremyException If the description or due date is missing.
     */
    public Deadline parseDeadline(String args) throws JeremyException {
        int byIndex = args.indexOf("/by");
        String description = byIndex >= 0 ? args.substring(0, byIndex).trim() : args.trim();
        String by = byIndex >= 0 ? args.substring(byIndex + 3).trim() : "";

        if (description.isEmpty() && by.isEmpty()) {
            throw new JeremyException(
                    "A deadline needs a description and a '/by' date/time, e.g. "
                            + "deadline submit report /by 11/10/2019 5pm");
        }
        if (description.isEmpty()) {
            throw new JeremyException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new JeremyException(
                    "A deadline needs a '/by' date/time, e.g. "
                            + "deadline " + description + " /by 11/10/2019 5pm");
        }
        return new Deadline(description, by);
    }

    /**
     * Returns an event parsed from the supplied command arguments.
     *
     * @param args Raw arguments following the event command.
     * @return Parsed event task.
     * @throws JeremyException If the description, start time, or end time is missing.
     */
    public Event parseEvent(String args) throws JeremyException {
        int fromIndex = args.indexOf("/from");
        int toIndex = args.indexOf("/to");
        String description = fromIndex >= 0 ? args.substring(0, fromIndex).trim() : args.trim();

        String from = "";
        String to = "";
        if (fromIndex >= 0 && toIndex > fromIndex) {
            from = args.substring(fromIndex + 5, toIndex).trim();
            to = args.substring(toIndex + 3).trim();
        } else if (fromIndex >= 0) {
            from = args.substring(fromIndex + 5).trim();
        }

        if (description.isEmpty()) {
            throw new JeremyException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new JeremyException(
                    "An event needs both '/from' and '/to' date/time, e.g. "
                            + "event " + description + " /from 2/10/2019 2pm /to 4pm");
        }
        return new Event(description, from, to);
    }
}
