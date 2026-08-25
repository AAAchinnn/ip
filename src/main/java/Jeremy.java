package jeremy;

import jeremy.exception.JeremyException;
import jeremy.parser.Parser;
import jeremy.storage.Storage;
import jeremy.task.Task;
import jeremy.task.TaskList;
import jeremy.task.Todo;
import jeremy.ui.Ui;

public class Jeremy {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public Jeremy(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (JeremyException e) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    public void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            String trimmed = input.trim();

            if (trimmed.equalsIgnoreCase("bye")) {
                ui.showBye();
                break;
            }

            if (trimmed.isEmpty()) {
                ui.showEmptyInputMessage();
                continue;
            }

            try {
                handleCommand(trimmed);
            } catch (JeremyException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private void handleCommand(String trimmed) throws JeremyException {
        String commandWord = parser.getCommandWord(trimmed);
        String args = parser.getArguments(trimmed);

        switch (commandWord) {
        case "list":
            ui.showTaskList(tasks.asList());
            break;
        case "mark":
            handleMark(args);
            break;
        case "unmark":
            handleUnmark(args);
            break;
        case "delete":
            handleDelete(args);
            break;
        case "todo":
            addTask(new Todo(parser.parseTodoDescription(args)));
            break;
        case "deadline":
            addTask(parser.parseDeadline(args));
            break;
        case "event":
            addTask(parser.parseEvent(args));
            break;
        default:
            throw new JeremyException(
                    "I don't recognize '" + commandWord
                            + "'. Try: todo, deadline, event, list, mark, unmark, delete, bye.");
        }
    }

    private void handleMark(String args) throws JeremyException {
        int index = parser.parseIndex(args, "mark");
        Task task = tasks.markDone(index);
        storage.save(tasks.asList());
        ui.showTaskMarked(task);
    }

    private void handleUnmark(String args) throws JeremyException {
        int index = parser.parseIndex(args, "unmark");
        Task task = tasks.markNotDone(index);
        storage.save(tasks.asList());
        ui.showTaskUnmarked(task);
    }

    private void handleDelete(String args) throws JeremyException {
        int index = parser.parseIndex(args, "delete");
        Task removed = tasks.delete(index);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removed, tasks.size());
    }

    private void addTask(Task newTask) {
        tasks.add(newTask);
        storage.save(tasks.asList());
        ui.showTaskAdded(newTask, tasks.size());
    }

    public static void main(String[] args) {
        new Jeremy("data/duke.txt").run();
    }
}
