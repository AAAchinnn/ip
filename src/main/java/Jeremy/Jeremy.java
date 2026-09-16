package jeremy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import jeremy.exception.JeremyException;
import jeremy.parser.Parser;
import jeremy.storage.Storage;
import jeremy.task.Task;
import jeremy.task.TaskList;
import jeremy.task.Todo;
import jeremy.ui.Ui;

public class Jeremy extends Application {

    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 600.0;
    private static final double SCROLL_PANE_WIDTH = 385.0;
    private static final double SCROLL_PANE_HEIGHT = 535.0;
    private static final double INPUT_WIDTH = 325.0;
    private static final double SEND_BUTTON_WIDTH = 55.0;
    private static final double LAYOUT_PADDING = 1.0;
    private static final double BOTTOM_SCROLL_VALUE = 1.0;
    private static final double DIALOG_SPACING = 6.0;
    private static final String WINDOW_TITLE = "Jeremy // Task Companion";
    private static final String WINDOW_STYLE = "-fx-background-color: #1f1e1d;";
    private static final String INPUT_STYLE = "-fx-background-color: #e9dfd0;"
            + " -fx-text-fill: #252323; -fx-font-family: 'Monospaced';"
            + " -fx-font-size: 13px;";
    private static final String BUTTON_STYLE = "-fx-background-color: #7c3f48;"
            + " -fx-text-fill: #f7efe3; -fx-font-family: 'Monospaced';"
            + " -fx-font-weight: bold; -fx-font-size: 12px;";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    /** Creates Jeremy with the default task data file for the JavaFX app. */
    public Jeremy() {
        this("data/duke.txt");
    }

    public Jeremy(String filePath) {
        assert filePath != null && !filePath.isBlank() : "Data file path must be provided";
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
        assert loadedTasks != null : "Task list must be initialized after loading";
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

    /** Builds and displays the JavaFX user interface. */
    @Override
    public void start(Stage stage) {
        assert stage != null : "JavaFX must provide a stage";

        AnchorPane mainLayout = createMainLayout();
        configureStage(stage, mainLayout);
        configureEventHandlers();
        stage.show();
    }

    private AnchorPane createMainLayout() {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        userInput.setPromptText("drop a command here...");
        sendButton = new Button("▶ SEND");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);
        mainLayout.setStyle(WINDOW_STYLE);

        configureLayout(mainLayout);
        return mainLayout;
    }

    private void configureStage(Stage stage, AnchorPane mainLayout) {
        stage.setScene(new Scene(mainLayout));
        stage.setTitle(WINDOW_TITLE);
        stage.setResizable(false);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
    }

    private void configureLayout(AnchorPane mainLayout) {
        mainLayout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        scrollPane.setPrefSize(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(BOTTOM_SCROLL_VALUE);
        scrollPane.setFitToWidth(true);
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setSpacing(DIALOG_SPACING);
        dialogContainer.setStyle(WINDOW_STYLE);

        userInput.setPrefWidth(INPUT_WIDTH);
        userInput.setStyle(INPUT_STYLE);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);
        sendButton.setStyle(BUTTON_STYLE);
        scrollPane.setStyle("-fx-background: #1f1e1d; -fx-border-color: #5d5a52;");
        AnchorPane.setTopAnchor(scrollPane, LAYOUT_PADDING);
        AnchorPane.setBottomAnchor(sendButton, LAYOUT_PADDING);
        AnchorPane.setRightAnchor(sendButton, LAYOUT_PADDING);
        AnchorPane.setLeftAnchor(userInput, LAYOUT_PADDING);
        AnchorPane.setBottomAnchor(userInput, LAYOUT_PADDING);
    }

    private void configureEventHandlers() {
        sendButton.setOnMouseClicked(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(BOTTOM_SCROLL_VALUE));
    }

    /** Adds the user's message and Jeremy's response, then clears the input. */
    private void handleUserInput() {
        assert userInput != null && dialogContainer != null : "GUI controls must be initialized";
        String userText = userInput.getText();
        String jeremyText = getResponse(userText);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText),
                DialogBox.getJeremyDialog(jeremyText));
        userInput.clear();
    }

    private void handleCommand(String trimmed) throws JeremyException {
        String commandWord = parser.getCommandWord(trimmed);
        String args = parser.getArguments(trimmed);

        switch (commandWord) {
        case "list":
            ui.showTaskList(tasks.asList());
            break;
        case "find":
            handleFind(args);
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
                            + "'. Try: todo, deadline, event, list, find, mark, unmark, delete, bye.");
        }
    }

    private void handleFind(String args) throws JeremyException {
        if (args.isEmpty()) {
            throw new JeremyException("What keyword should I search for? Use: find <keyword>.");
        }
        ui.showMatchingTasks(tasks.find(args));
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

    private void addTask(Task newTask) throws JeremyException {
        if (tasks.containsEquivalent(newTask)) {
            throw new JeremyException("A task with the same details is already on the list.");
        }
        List<Task> conflicts = ScheduleConflictDetector.findConflicts(newTask, tasks.asList());
        tasks.add(newTask);
        storage.save(tasks.asList());
        if (!conflicts.isEmpty()) {
            ui.showScheduleConflict(conflicts);
        }
        ui.showTaskAdded(newTask, tasks.size());
    }

    /** Processes a GUI command using the same parser and task data as the CLI. */
    public String getResponse(String input) {
        assert input != null : "GUI command input must not be null";
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return "No signal came through — type a command, or 'bye' to exit.";
        }
        if (trimmed.equalsIgnoreCase("bye")) {
            return "Session over. Keep your deadlines loud and your stress low.";
        }

        try {
            String commandWord = parser.getCommandWord(trimmed);
            String args = parser.getArguments(trimmed);

            switch (commandWord) {
            case "list":
                return formatTaskList("Setlist of tasks:", tasks.asList(), "No tasks on the setlist yet.");
            case "find":
                if (args.isEmpty()) {
                    throw new JeremyException("What keyword should I search for? Use: find <keyword>.");
                }
                return formatTaskList("Matching tracks:", tasks.find(args), "No matches in the setlist.");
            case "mark":
                return formatMarkedTask(parser.parseIndex(args, "mark"), true);
            case "unmark":
                return formatMarkedTask(parser.parseIndex(args, "unmark"), false);
            case "delete":
                return formatDeletedTask(parser.parseIndex(args, "delete"));
            case "todo":
                return formatAddedTask(new Todo(parser.parseTodoDescription(args)));
            case "deadline":
                return formatAddedTask(parser.parseDeadline(args));
            case "event":
                return formatAddedTask(parser.parseEvent(args));
            default:
                throw new JeremyException(
                        "That command missed the beat: '" + commandWord
                                + "'. Try: todo, deadline, event, list, find, mark, unmark, delete, bye.");
            }
        } catch (JeremyException e) {
            return e.getMessage();
        }
    }

    private String formatTaskList(String heading, List<Task> taskList, String emptyMessage) {
        if (taskList.isEmpty()) {
            return emptyMessage;
        }
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < taskList.size(); i++) {
            response.append("\n").append(i + 1).append(". ").append(taskList.get(i));
        }
        return response.toString();
    }

    private String formatAddedTask(Task task) throws JeremyException {
        if (tasks.containsEquivalent(task)) {
            throw new JeremyException("A task with the same details is already on the list.");
        }
        List<Task> conflicts = ScheduleConflictDetector.findConflicts(task, tasks.asList());
        tasks.add(task);
        storage.save(tasks.asList());
        String warning = conflicts.isEmpty() ? "" : formatConflictWarning(conflicts) + "\n";
        return warning + "Locked in. Added to the setlist:\n" + task
                + "\nNow you have " + tasks.size() + " task(s) in the list.";
    }

    private String formatConflictWarning(List<Task> conflicts) {
        StringBuilder warning = new StringBuilder("Heads up: this task may clash with:");
        for (Task conflict : conflicts) {
            warning.append("\n- ").append(conflict);
        }
        return warning.toString();
    }

    private String formatMarkedTask(int index, boolean markDone) throws JeremyException {
        Task task = markDone ? tasks.markDone(index) : tasks.markNotDone(index);
        storage.save(tasks.asList());
        String action = markDone ? "done" : "not done";
        return "Status updated — task marked as " + action + ":\n" + task;
    }

    private String formatDeletedTask(int index) throws JeremyException {
        Task removed = tasks.delete(index);
        storage.save(tasks.asList());
        return "Cleared from the setlist:\n" + removed
                + "\nNow you have " + tasks.size() + " task(s) in the list.";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
