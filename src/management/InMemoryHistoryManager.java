package management;

import model.Task;
import model.HistoryLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HistoryLinkedList historyLinkedList;

    public InMemoryHistoryManager() {
        this.historyLinkedList = new HistoryLinkedList();
    }

    @Override
    public <T extends Task> void add(T task) {
        historyLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyLinkedList.removeTaskById(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyLinkedList.getTasks();
    }
}


