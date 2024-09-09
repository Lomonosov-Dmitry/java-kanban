package management;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int counter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private final TreeSet<Task> timesortedTasks;


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.counter = 1;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.timesortedTasks = new TreeSet<>(new CompareByTime());
    }


    protected void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public void addTask(Task newTask) {
        if (timeValidator(newTask)) {
            newTask.setId(counter);
            tasks.put(counter, newTask);
            counter++;
            if (newTask.getStartTime() != null)
                timesortedTasks.add(newTask);
        }
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
        if (epics.containsKey(subTask.getEpicId()) && timeValidator(subTask)) {
            subTask.setId(counter);
            subTasks.put(counter, subTask);
            epics.get(subTask.getEpicId()).addSubTask(counter);
            counter++;
            updateEpicStatus(subTask.getEpicId());
            if (subTask.getStartTime() != null)
                timesortedTasks.add(subTask);
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
    public Optional<Task> getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return Optional.of(task);
        } else
            return Optional.empty();
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return Optional.of(epic);
        } else
            return Optional.empty();
    }

    @Override
    public Optional<SubTask> getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
            return Optional.of(subTask);
        } else
            return Optional.empty();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(timesortedTasks);
    }

    @Override
    public void clearTasks() {
        clearHistory("task");
        tasks.clear();
        timesortedTasks.removeIf(timesortedTask -> timesortedTask.getClass() == Task.class);
    }

    @Override
    public void clearEpics() {
        clearHistory("epic");
        clearHistory("subTask");
        epics.clear();
        subTasks.clear();
        timesortedTasks.removeIf(timesortedTask -> timesortedTask.getClass() == SubTask.class);
    }

    @Override
    public void clearEpicSubTasks(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> epicSubTasks = epics.get(epicId).getSubTasks();
            for (Integer subId : epicSubTasks) {
                if (getSubTaskById(subId).isPresent())
                    timesortedTasks.remove(getSubTaskById(subId).get());
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
            clearEpicSubTasks(epic.getId());
            updateEpicStatus(epic.getId());
        }
        subTasks.clear();
    }

    @Override
    public void removeTask(int taskId) {
        if (getTaskById(taskId).isPresent())
            timesortedTasks.remove(getTaskById(taskId).get());
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            clearEpicSubTasks(epicId);
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void removeSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            if (getSubTaskById(subTaskId).isPresent())
                timesortedTasks.remove(getSubTaskById(subTaskId).get());
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).removeSubTask(subTaskId);
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
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
        updateEpicTime(epicId);

    }

    protected void updateEpicTime(int epicId) {
        Epic epic = epics.get(epicId);
        List<SubTask> subs = getSubTaskByEpic(epicId);
        if (subs != null) {
            LocalDateTime epicStart = null;
            Duration duration = null;
            epic.setEndTime(null);
            for (Task sub : subs) {
                if (sub.getStartTime() != null) {
                    if (sub.getStartTime().isBefore(epicStart))
                        epicStart = sub.getStartTime();
                    duration.plus(sub.getDuration());
                }
            }
            if (epicStart != null) {
                epic.setStartTime(epicStart);
                epic.setDuration(duration);
                epic.setEndTime(epicStart.plus(duration));
            } else
                epic.setEndTime(null);
        }
    }

    protected boolean timeValidator(Task newTask) {
        long count = getPrioritizedTasks().stream()
                .filter(task -> task.getEndTime().isAfter(newTask.getStartTime()))
                .filter(task -> task.getStartTime().isBefore(newTask.getEndTime()))
                .count();
        return count <= 0;
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
