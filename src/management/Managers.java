package management;

public class Managers {

    private TaskManager taskManager;
    private final HistoryManager historyManager;

    public Managers() {
        this.taskManager = new InMemoryTaskManager(this);
        this.historyManager = new InMemoryHistoryManager();
    }

    public TaskManager getDefault(){
        return taskManager;
    }

    public HistoryManager getDefaultHistory(){
        return historyManager;
    }
}
