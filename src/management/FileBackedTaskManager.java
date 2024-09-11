package management;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String file;

    public FileBackedTaskManager(HistoryManager historyManager, String file) {
        super(historyManager);
        this.file = file;
        startFileOperations();
    }

    private void startFileOperations() throws ManagerSaveException {
        if (!Files.exists(Path.of(file))) {
            try {
                Files.createFile(Path.of(file));
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка создания файла");
            }
        } else {
            loadFile();
        }
    }

    private void loadFile() {
        List<String> tasksFromFile = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                tasksFromFile.add(reader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        if (!tasksFromFile.isEmpty())
            loadTasks(tasksFromFile);
    }

    private void loadTasks(List<String> tasksFromFile) {
        int counter = 0;
        for (String s : tasksFromFile) {
            String[] taskSource = s.split(",");
            counter = Integer.parseInt(taskSource[0]);
            TaskStatus status = TaskStatus.valueOf(taskSource[3]);
            LocalDateTime time = null;
            Duration duration = null;
            if (taskSource.length > 5) {
                time = LocalDateTime.parse(taskSource[6], DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm"));
                duration = Duration.ofMinutes(Long.parseLong(taskSource[7]));
            }
            switch (taskSource[1]) {
                case "TASK": {
                    super.addTaskWithId(new Task(counter, taskSource[2], taskSource[4], status, time, duration));
                    break;
                }
                case "EPIC": {
                    super.addEpicWithId(new Epic(counter, taskSource[2], taskSource[4]));
                    break;
                }
                case "SUBTASK": {
                    super.addSubTaskWithId(new SubTask(counter, taskSource[2], taskSource[4], status, Integer.parseInt(taskSource[5]), time, duration));
                    break;
                }
            }
        }
        setCounter(counter + 1);
    }

    private void saveFile() {
        List<String> tasks = saveTasks();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic,start_time,duration");
            writer.newLine();
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    private List<String> saveTasks() {
        List<Task> arrangedById = new ArrayList<>();
        arrangedById.addAll(getAllTasks());
        arrangedById.addAll(getAllEpics());
        arrangedById.addAll(getAllSubTasks());
        arrangedById.sort(new CompareById());
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

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        saveFile();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        saveFile();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        saveFile();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        saveFile();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        saveFile();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        saveFile();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        saveFile();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        saveFile();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        saveFile();
    }
}


