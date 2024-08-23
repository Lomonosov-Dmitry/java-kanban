package management;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    Managers managers = new Managers();
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), "c:\\123\\tasks.txt");
    Task task = new Task("TaskName", "TaskDes", TaskStatus.NEW);
    Epic epic = new Epic("EpicName", "EpicDes");
    SubTask subTask = new SubTask("SubName", "SubDes", TaskStatus.NEW, 2);

    @BeforeEach
    void newFileManager() {
        fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), "c:\\123\\tasks.txt");
    }


    @Test
    void TaskShouldBeAddedToFile() {
        fileBackedTaskManager.addTask(task);
        fileBackedTaskManager.addEpic(epic);
        fileBackedTaskManager.addSubTask(subTask);
    }

}