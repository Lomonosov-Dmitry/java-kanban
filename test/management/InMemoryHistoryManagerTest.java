package management;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    Managers managers = new Managers();

    @Test
    void allTasksShouldBeAddedToHistory() {
        Task task1 = new Task(0, "aaa", "bbb", TaskStatus.NEW);
        Task task2 = new Task(1, "sss", "ddd", TaskStatus.NEW);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(task1.getName(), tasks.get(1).getName(), "Имена не совпадают!");
        assertEquals(task1.getDescription(), tasks.get(1).getDescription(), "Описания не совпадают!");
        assertEquals(task1.getStatus(), tasks.get(1).getStatus(), "Статусы не совпадают!");

        assertEquals(task2.getName(), tasks.get(0).getName(), "Имена не совпадают!");
        assertEquals(task2.getDescription(), tasks.get(0).getDescription(), "Описания не совпадают!");
        assertEquals(task2.getStatus(), tasks.get(0).getStatus(), "Статусы не совпадают!");
    }

    @Test
    void equalTaskShouldBeDeleted() {
        Task task1 = new Task(0, "aaa", "bbb", TaskStatus.NEW);
        Task task2 = new Task(0, "sss", "ddd", TaskStatus.NEW);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(1, tasks.size(), "Длина массива не совпадает!");
        assertEquals(task2.getName(), tasks.get(0).getName(), "Имена не совпадают!");
        assertEquals(task2.getDescription(), tasks.get(0).getDescription(), "Описания не совпадают!");
        assertEquals(task2.getStatus(), tasks.get(0).getStatus(), "Статусы не совпадают!");
    }

    @Test
    void taskShouldBeRemoved() {
        Task task1 = new Task(0, "aaa", "bbb", TaskStatus.NEW);
        Task task2 = new Task(1, "sss", "ddd", TaskStatus.NEW);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(0);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(1, tasks.size(), "Длина массива не совпадает!");
        assertEquals(task2.getName(), tasks.get(0).getName(), "Имена не совпадают!");
        assertEquals(task2.getDescription(), tasks.get(0).getDescription(), "Описания не совпадают!");
        assertEquals(task2.getStatus(), tasks.get(0).getStatus(), "Статусы не совпадают!");
    }

}