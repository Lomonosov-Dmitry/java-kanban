package management;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final String file;

    public FileBackedTaskManager(HistoryManager historyManager, String file){
        super(historyManager);
        this.file = file;
        startFileOperations();
    }

    private void startFileOperations() throws ManagerSaveException {
        if (!Files.exists(Path.of(file))) {
            try {
                Files.createFile(Path.of(file));
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        } else {
            loadFile();
        }
    }

    private void loadFile() {
        List<String> tasksFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                tasksFromFile.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        if (!tasksFromFile.isEmpty())
            loadTasks(tasksFromFile);
    }

    private void loadTasks(List<String> tasksFromFile) {
        for (String s : tasksFromFile) {
            String[] taskSource = s.split(",");
            TaskStatus status = null;
            switch (taskSource[3]) {
                case "NEW": {
                    status = TaskStatus.NEW;
                    break;
                }
                case "IN_PROGRESS": {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
                case "DONE": {
                    status = TaskStatus.DONE;
                    break;
                }
            }
            switch (taskSource[1]) {
                case "TASK": {
                    super.addTask(new Task(Integer.parseInt(taskSource[0]), taskSource[2], taskSource[4], status));
                    break;
                }
                case "EPIC": {
                    super.addEpic(new Epic(Integer.parseInt(taskSource[0]), taskSource[2], taskSource[4]));
                    break;
                }
                case "SUBTASK": {
                    super.addSubTask(new SubTask(Integer.parseInt(taskSource[0]), taskSource[2], taskSource[4], status, Integer.parseInt(taskSource[5])));
                    break;
                }
            }
        }
    }

    private void saveFile() {
        List<String> tasks = saveTasks();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private void save(Task task) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
                writer.write(task.toCSV());
                writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private List<String> saveTasks() {
        List<Task> arrangedById = new ArrayList<>();
        arrangedById.addAll(getAllTasks());
        arrangedById.addAll(getAllEpics());
        arrangedById.addAll(getAllSubTasks());
        arrangedById.sort(new compareById());
        List<String> tasksCSV = new ArrayList<>();
        for (Task task : arrangedById) {
            tasksCSV.add(task.toCSV());
        }
        return tasksCSV;
    }

    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        saveFile();
    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        saveFile();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        saveFile();
    }
}

class compareById implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getId() > task2.getId())
            return 1;
        else
            return -1;
    }
}
