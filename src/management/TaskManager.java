package management;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task newTask);

    void addEpic(Epic newEpic);

    void addSubTask(SubTask subTask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void clearTasks();

    void clearEpics();

    void clearEpicSubTasks(int epicId);

    void clearSubTasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubTask(int subTaskId);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTaskByEpic(int epicId);

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubTask(SubTask newSubTask);

    void updateEpicStatus(int epicId);
}
