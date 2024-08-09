import management.Managers;
import management.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;


public class Main {

    public static void main(String[] args) {

        Managers manager = new Managers();
        TaskManager taskManager = manager.getDefault();

        Task task1 = new Task("Task1", "aaa", TaskStatus.NEW);
        Task task2 = new Task("Task2", "sss", TaskStatus.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Epic1", "aaaa"); //id=3
        Epic epic2 = new Epic("Epic2", "ssss");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("SubTask1", "aaaaa", TaskStatus.NEW, 3);
        SubTask subTask2 = new SubTask("SubTask2", "sssss", TaskStatus.NEW, 3);
        SubTask subTask3 = new SubTask("SubTask3", "ddddd", TaskStatus.NEW, 3);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getHistory());

        taskManager.removeTask(2);
        System.out.println(taskManager.getHistory());

        taskManager.removeEpic(3);
        System.out.println(taskManager.getHistory());
    }
}

