package management;

import model.Epic;
import model.Task;
import model.TaskStatus;
import model.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int counter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.counter = 1;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }


    protected void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public void addTask(Task newTask) {
        newTask.setId(counter);
        tasks.put(counter, newTask);
        counter++;
    }

    protected void addTaskWithId(Task newTask) {
        if (!tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void addEpic(Epic newEpic) {
        newEpic.setId(counter);
        epics.put(counter, newEpic);
        counter++;
    }

    protected void addEpicWithId(Epic newEpic) {
        if (!epics.containsKey(newEpic.getId())) {
            epics.put(newEpic.getId(), newEpic);
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(counter);
            subTasks.put(counter, subTask);
            epics.get(subTask.getEpicId()).addSubTask(counter);
            counter++;
            updateEpicStatus(subTask.getEpicId());
        }
    }

    protected void addSubTaskWithId(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask.getId());
            updateEpicStatus(subTask.getEpicId());
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public void clearTasks() {
        clearHistory("task");
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        clearHistory("epic");
        clearHistory("subTask");
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearEpicSubTasks(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> epicSubTasks = epics.get(epicId).getSubTasks();
            for (Integer subId : epicSubTasks) {
                subTasks.remove(subId);
                historyManager.remove(subId);
            }
            epics.get(epicId).clearSubTasks();
        }
        updateEpicStatus(epicId);
    }

    @Override
    public void clearSubTasks() {
        clearHistory("subTask");
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void removeTask(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subs = epics.get(epicId).getSubTasks();
            for (Integer subNum : subs) {
                subTasks.remove(subNum);
                historyManager.remove(subNum);
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void removeSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).removeSubTask(subTaskId);
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        return allTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        allEpics.addAll(epics.values());
        return allEpics;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        allSubTasks.addAll(subTasks.values());
        return allSubTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTaskByEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<SubTask> epicSubTasks = new ArrayList<>();
            ArrayList<Integer> subTasksId = epics.get(epicId).getSubTasks();
            for (Integer id : subTasksId) {
                epicSubTasks.add(subTasks.get(id));
            }
            return epicSubTasks;
        }
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task newTask) {
        tasks.replace(newTask.getId(), newTask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic epicForUpdate = epics.get(newEpic.getId());
        if (epicForUpdate != null) {
            epicForUpdate.setName(newEpic.getName());
            epicForUpdate.setDescription(newEpic.getDescription());
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        SubTask oldSubTask = subTasks.get(newSubTask.getId());
        if (oldSubTask != null) {
            if (oldSubTask.getEpicId() == newSubTask.getEpicId()) {
                subTasks.replace(newSubTask.getId(), newSubTask);
                updateEpicStatus(newSubTask.getEpicId());
            }
        }
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> epicSubTasks = epic.getSubTasks();
        int statusNew = 0;
        int statusDone = 0;
        for (int subTaskId : epicSubTasks) {
            if (subTasks.get(subTaskId).getStatus() == TaskStatus.NEW)
                statusNew++;
            else if (subTasks.get(subTaskId).getStatus() == TaskStatus.DONE) {
                statusDone++;
            }
        }
        if (statusNew == epicSubTasks.size())
            epic.setStatus(TaskStatus.NEW);
        else if (statusDone == epicSubTasks.size())
            epic.setStatus(TaskStatus.DONE);
        else
            epic.setStatus(TaskStatus.IN_PROGRESS);


    }

    private void clearHistory(String taskType) {
        switch (taskType) {
            case "task": {
                for (Integer taskId : tasks.keySet()) {
                    historyManager.remove(taskId);
                    break;
                }
            }
            case "epic": {
                for (Integer epicId : epics.keySet()) {
                    historyManager.remove(epicId);
                }
            }
            case "subTask": {
                for (Integer subId : subTasks.keySet()) {
                    historyManager.remove(subId);
                }
            }
        }
    }

}
