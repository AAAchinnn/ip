# Jeremy

Jeremy is a grunge-themed personal task companion for organising todos,
deadlines, and events. It provides a JavaFX chat interface and stores tasks
between runs.

> Keep your tasks loud and your stress low.

## Features

- Add todo tasks, deadlines, and events
- List all saved tasks
- Search tasks by keyword
- Mark and unmark tasks
- Delete tasks
- Save tasks automatically to the data file
- Validate numeric dates and times
- Reject duplicate task details
- Warn when scheduled events may clash
- Display user messages and Jeremy's responses in the JavaFX GUI

## Getting started

### Requirements

- Java 21 or later
- macOS, Windows, or Linux

### Run the application

From the project root, run:

```bash
./gradlew run
```

The application opens the Jeremy GUI. Type a command in the input field and
press **Enter** or select **Send**.

The interface uses a retro, text-only visual style with monospaced lettering,
charcoal backgrounds, burgundy accents, and olive user messages. The window
can be resized, the conversation scrolls to the latest response, and the
input field and send button provide focus and hover feedback. Error responses
are shown with a red warning style and a `⚠` marker.

### Build and test

Run the automated tests with:

```bash
./gradlew clean test
```

The tests are stored under `src/test/java` and use temporary files for
persistence checks, so they do not modify the real `data/` directory.

Create the executable fat JAR with:

```bash
./gradlew clean shadowJar
```

The output is generated at `build/libs/jeremy.jar`.

## Commands

| Command | Description | Example |
| --- | --- | --- |
| `todo <description>` | Adds a todo task | `todo read CS2103 notes` |
| `deadline <description> /by <date/time>` | Adds a deadline | `deadline submit report /by 18/9/2026 5pm` |
| `event <description> /from <date/time> /to <date/time>` | Adds an event | `event project meeting /from 18/9/2026 2pm /to 4pm` |
| `list` | Shows all tasks | `list` |
| `find <keyword>` | Searches task descriptions | `find report` |
| `mark <number>` | Marks a task as done | `mark 1` |
| `unmark <number>` | Marks a task as not done | `unmark 1` |
| `delete <number>` | Deletes a task | `delete 1` |
| `bye` | Ends the current session | `bye` |

Task numbers are one-based and follow the order shown by `list`.

## Input validation

Jeremy gives an error message when it receives invalid input, including:

1. An unknown command or missing argument
2. An invalid task number
3. A missing or repeated scheduling parameter
4. A non-existent numeric date, such as `31/2/2026`
5. An invalid clock time
6. An event whose end is not later than its start
7. A task with the same details as an existing task

Leading, trailing, and repeated spaces are normalized where possible. Saved
data that cannot be read is reported without crashing the application.

## Data storage

Tasks are saved in `data/duke.txt` relative to the project root. The data file
is created or updated when a task is added, marked, unmarked, or deleted.

Do not commit personal task data. The `data/` directory is runtime data and
should be excluded from commits unless a deliberately empty placeholder is
needed.

## Project structure

```text
src/
├── main/java/jeremy/
│   ├── Jeremy.java
│   ├── DialogBox.java
│   ├── RetroTheme.java
│   ├── parser/
│   ├── storage/
│   ├── task/
│   ├── ui/
│   └── exception/
└── test/java/jeremy/
    ├── parser/
    ├── storage/
    └── task/
```

The main entry point is `jeremy.Jeremy`.

## Credits

Jeremy was developed as an individual project for CS2103T. The JavaFX
interface follows the course JavaFX tutorial structure. Any reused code or
external resources should be credited in the relevant source file or commit.
