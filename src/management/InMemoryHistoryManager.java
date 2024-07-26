package management;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> taskHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new ArrayList<>();
    }

    @Override
    public <T extends Task> void add(T task) {
        if (task != null) {
            if (taskHistory.size() == 10) {
                taskHistory.removeFirst();
                taskHistory.add(task);
            } else
                taskHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskHistory;
    }
}
