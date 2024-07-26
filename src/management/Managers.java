package management;

public class Managers {

    private TaskManager taskManager;
    private final HistoryManager historyManager;

    public Managers() {
        this.historyManager = new InMemoryHistoryManager();
        this.taskManager = new InMemoryTaskManager(historyManager);
    }

    public TaskManager getDefault(){
        return taskManager;
    }

    public HistoryManager getDefaultHistory(){
        return historyManager;
    }
}
