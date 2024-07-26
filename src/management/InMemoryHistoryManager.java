package management;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> taskHistory;

    public InMemoryHistoryManager() {
        this.taskHistory = new ArrayList<>();
    }

    //Если имелась ввиду проверка на null в вызывающих методах, то мне кажется это не целесообразным.
    //Писать больше (3 метода править), а проверка так или иначе будет выполняться только для этого метода по-сути.
    //Логика работы методов получения задачи по ID предполагает получение на выходе задачи или null, если не найдено.
    //Возможно я чего-то не понял.
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
    public List<Task> getHistory() {
        return new ArrayList<>(taskHistory);
    }
}
