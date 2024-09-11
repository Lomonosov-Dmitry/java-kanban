package management;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    void epicStatusCountingTest(TaskManager manager) {
        Epic epic = new Epic("EpicName", "EpicDes");
        SubTask subTask = new SubTask("SubName", "SubDes", TaskStatus.NEW, 1);
        SubTask subTask2 = new SubTask("SubName2", "SubDes", TaskStatus.NEW, 1);
        SubTask subTask3 = new SubTask("SubName3", "SubDes", TaskStatus.NEW, 1);

        manager.addEpic(epic);
        manager.addSubTask(subTask);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        assertEquals(epic.getStatus(), TaskStatus.NEW, "Статус должен быть NEW");

        subTask.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(subTask);
        subTask2.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subTask2);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Статус должен быть IN_PROGRESS");

        subTask.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subTask);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Статус должен быть IN_PROGRESS");

        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS, "Статус должен быть IN_PROGRESS");

        subTask3.setStatus(TaskStatus.DONE);
        manager.updateSubTask(subTask3);
        assertEquals(epic.getStatus(), TaskStatus.DONE, "Статус должен быть DONE");
    }

    void timeManagementTest(TaskManager manager) {
        Task task1 = new Task("TaskName1", "TaskDes", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(120));
        Task task2 = new Task("TaskName2", "TaskDes", TaskStatus.NEW, LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(60));
        Task task3 = new Task("TaskName3", "TaskDes", TaskStatus.NEW, LocalDateTime.now().plusMinutes(121), Duration.ofMinutes(60));

        manager.addTask(task1);
        manager.addTask(task2);
        assertEquals(manager.getAllTasks().size(), 1, "Должна быть добавлена только одна задача!");

        manager.addTask(task3);
        assertEquals(manager.getAllTasks().size(), 2, "Должно быть две задачи!");

        task2.setStartTime(LocalDateTime.now().plusMinutes(130));
        manager.addTask(task2);
        assertEquals(manager.getAllTasks().size(), 2, "Должно быть две задачи!");
    }
}