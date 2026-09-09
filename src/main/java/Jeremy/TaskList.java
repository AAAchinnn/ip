package jeremy.task;

import java.util.ArrayList;
import java.util.List;

import jeremy.exception.JeremyException;

/**
 * Contains the task list and the operations to add, delete, and
 * mark/unmark tasks in it.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        assert tasks != null : "Initial task list must not be null";
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        assert task != null : "Cannot add a null task";
        tasks.add(task);
    }

    public Task delete(int index) throws JeremyException {
        checkIndex(index, "delete");
        return tasks.remove(index - 1);
    }

    public Task markDone(int index) throws JeremyException {
        checkIndex(index, "mark");
        Task task = tasks.get(index - 1);
        task.markAsDone();
        return task;
    }

    public Task markNotDone(int index) throws JeremyException {
        checkIndex(index, "unmark");
        Task task = tasks.get(index - 1);
        task.markAsNotDone();
        return task;
    }

    public Task get(int index) throws JeremyException {
        checkIndex(index, "access");
        return tasks.get(index - 1);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns the underlying list. Callers should treat this as read-mostly. */
    public List<Task> asList() {
        return tasks;
    }

    public List<Task> find(String keyword) {
        assert keyword != null : "Search keyword must not be null";
        List<Task> matchingTasks = new ArrayList<>();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
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
