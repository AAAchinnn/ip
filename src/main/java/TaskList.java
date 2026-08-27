package jeremy.task;

import java.util.ArrayList;
import java.util.List;

import jeremy.exception.JeremyException;

/**
 * Stores tasks and provides operations to add, delete, and mark tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing copies of the supplied list's task references.
     *
     * @param tasks Tasks with which to initialize the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified one-based index.
     *
     * @param index One-based task index.
     * @return Removed task.
     * @throws JeremyException If the list is empty or the index is invalid.
     */
    public Task delete(int index) throws JeremyException {
        checkIndex(index, "delete");
        return tasks.remove(index - 1);
    }

    /**
     * Marks the task at the specified one-based index as done.
     *
     * @param index One-based task index.
     * @return The marked task.
     * @throws JeremyException If the list is empty or the index is invalid.
     */
    public Task markDone(int index) throws JeremyException {
        checkIndex(index, "mark");
        Task task = tasks.get(index - 1);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the specified one-based index as not done.
     *
     * @param index One-based task index.
     * @return The unmarked task.
     * @throws JeremyException If the list is empty or the index is invalid.
     */
    public Task markNotDone(int index) throws JeremyException {
        checkIndex(index, "unmark");
        Task task = tasks.get(index - 1);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the task at the specified one-based index.
     *
     * @param index One-based task index.
     * @return Task at the requested index.
     * @throws JeremyException If the list is empty or the index is invalid.
     */
    public Task get(int index) throws JeremyException {
        checkIndex(index, "access");
        return tasks.get(index - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the task list contains no tasks.
     *
     * @return {@code true} when the list is empty; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the underlying list of tasks. Callers should treat it as read-mostly. */
    public List<Task> asList() {
        return tasks;
    }

    private void checkIndex(int index, String actionWord) throws JeremyException {
        if (tasks.isEmpty()) {
            throw new JeremyException("Your list is empty — nothing to " + actionWord + ".");
        }
        if (index < 1 || index > tasks.size()) {
            throw new JeremyException(
                    "That task number doesn't exist. You have " + tasks.size()
                            + " task(s) — pick between 1 and " + tasks.size() + ".");
        }
    }
}
