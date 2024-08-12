package management;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    Managers managers = new Managers();
    Task task = new Task("aaa", "bbb", TaskStatus.NEW);
    Epic epic = new Epic("aaa", "bbb");
    SubTask subTask = new SubTask("sss", "ddd", TaskStatus.NEW, 2);

    @BeforeEach
    void beforeEach(){
        managers.getDefault().clearTasks();
        managers.getDefault().clearEpics();
        managers.getDefault().clearSubTasks();
    }
    @Test
    void allTasksShuoldBeAddedAndFound(){
        managers.getDefault().addTask(task);
        managers.getDefault().addEpic(epic);
        managers.getDefault().addSubTask(subTask);

        assertEquals(task, managers.getDefault().getTaskById(1), "Задача не найдена!");
        assertEquals(epic, managers.getDefault().getEpicById(2), "Эпик не найден!");
        assertEquals(subTask, managers.getDefault().getSubTaskById(3), "Подзадача не найдена!");
    }

    @Test
    void taskShouldBeCorrectlyAddedToHistory(){
        managers.getDefault().addTask(task);
        managers.getDefault().addEpic(epic);
        managers.getDefault().getTaskById(1);
        managers.getDefault().getEpicById(2);

        List<Task> tasks = managers.getDefaultHistory().getHistory();
        assertEquals(task.getName(), tasks.get(0).getName(), "Имена не совпадают!");
        assertEquals(task.getDescription(), tasks.get(0).getDescription(), "Описания не совпадают!");
        assertEquals(task.getStatus(), tasks.get(0).getStatus(), "Статусы не совпадают!");
    }
}