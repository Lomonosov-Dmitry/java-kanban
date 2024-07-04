import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    public TaskManager() {
        this.counter = 0;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    public void addTask(Task newTask) {
        newTask.setId(counter);
        tasks.put(counter, newTask);
        counter++;
    }

    public void addEpic(Epic newEpic) {
        newEpic.setId(counter);
        epics.put(counter, newEpic);
        counter++;
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(counter);
        subTasks.put(counter, subTask);
        getEpicById(subTask.getEpicId()).addSubTask(counter);
        counter++;
        updateEpicStatus(subTask.getEpicId());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
    }

    public void clearEpicSubTasks(int epicId) {
        if (epics.containsKey(epicId))
            epics.get(epicId).clearSubTasks();
        updateEpicStatus(epicId);
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTasks();
            updateEpicStatus(epic.getId());
        }
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) {
        ArrayList<Integer> subs = getEpicById(epicId).getSubTasks();
        for (Integer subNum : subs) {
            subTasks.remove(subNum);
        }
        epics.remove(epicId);
    }

    public void removeSubTask(int subTaskId) {
        int epicId = getSubTaskById(subTaskId).getEpicId();
        getEpicById(epicId).removeSubTask(subTaskId);
        subTasks.remove(subTaskId);
        updateEpicStatus(epicId);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        return allTasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        allEpics.addAll(epics.values());
        return allEpics;
    }

    public ArrayList<SubTask> getAllSubTasks(){
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        allSubTasks.addAll(subTasks.values());
        return allSubTasks;
    }

    public ArrayList<SubTask> getSubTaskByEpic (int epicId){
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if(subTask.getEpicId() == epicId)
                epicSubTasks.add(subTask);
        }
        return epicSubTasks;
    }

    public void updateTask(Task newTask) {
        tasks.replace(newTask.getId(), newTask);
    }

    //Тут можно было пойти заменой всего объекта, но смысла не вижу, мы можем обновлять всего два поля.
    public void updateEpic(Epic newEpic) {
        Epic epicForUpdate = epics.get(newEpic.getId());
        epicForUpdate.setName(newEpic.getName());
        epicForUpdate.setDescription(newEpic.getDescription());
    }

    public void updateSubTask(SubTask newSubTask) {
        subTasks.replace(newSubTask.getId(), newSubTask);
        updateEpicStatus(newSubTask.getEpicId());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = getEpicById(epicId);
        ArrayList<Integer> subTasks = epic.getSubTasks();
        int statusNew = 0;
        int statusDone = 0;
        for (int subTaskId : subTasks) {
            if (getSubTaskById(subTaskId).getStatus() == TaskStatus.NEW)
                statusNew++;
            else if (getSubTaskById(subTaskId).getStatus() == TaskStatus.DONE) {
                statusDone++;
            }
        }
        if (statusNew == subTasks.size())
            epic.setStatus(TaskStatus.NEW);
        else if (statusDone == subTasks.size())
            epic.setStatus(TaskStatus.DONE);
        else
            epic.setStatus(TaskStatus.IN_PROGRESS);


    }


}
