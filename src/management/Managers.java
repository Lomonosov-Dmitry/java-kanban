package management;

public class Managers {

    private TaskManager taskManager;
    private static HistoryManager historyManager = null;

    public Managers() {
        this.historyManager = new InMemoryHistoryManager();
        this.taskManager = new InMemoryTaskManager(historyManager);
    }

    public TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
