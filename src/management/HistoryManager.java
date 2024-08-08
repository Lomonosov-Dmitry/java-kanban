package management;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    <T extends Task>void add(T task);
    void remove(int id);

    List<Task> getHistory();
}
