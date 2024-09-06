package management;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    Managers managers = new Managers();
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), "c:\\123\\tasks.txt");
    Task task1 = new Task("TaskName1", "TaskDes", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(600));
    Task task2 = new Task("TaskName2", "TaskDes", TaskStatus.NEW, LocalDateTime.now().plusMinutes(601), Duration.ofMinutes(600));
    Epic epic = new Epic("EpicName", "EpicDes");
    SubTask subTask = new SubTask("SubName", "SubDes", TaskStatus.NEW, 2, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(300));
    SubTask subTask2 = new SubTask("SubName2", "SubDes", TaskStatus.NEW, 2, LocalDateTime.now(), Duration.ofMinutes(200));

    @Test
    void epicStatusCountingTest() throws IOException {
        Files.deleteIfExists(Path.of("c:\\123\\tasks.txt"));
        super.epicStatusCountingTest(new FileBackedTaskManager(Managers.getDefaultHistory(), "c:\\123\\tasks.txt"));
    }

    @Test
    void timeManagementTest() {
        super.timeManagementTest(fileBackedTaskManager);
    }

//    @Test
//    void exceptionTest() {
//        assertThrows(ManagerSaveException.class, () -> {
//            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), "c:\\321\\tasks.txt");
//        }, "Неверно указанный путь должен вызывать исключение");
//    }

}