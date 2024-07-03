import java.util.HashMap;

public class TaskManager {
    private int counter;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        this.counter = 0;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
    }


    public void addTask(String name, String description){
        Task newTask = new Task(counter, name, description);
        tasks.put(counter, newTask);
        counter++;
    }

    public void addEpic(String name, String description){
        Epic newEpic = new Epic(counter, name, description);
        epics.put(counter, newEpic);
        counter++;
    }

    public void addSubTask(String name, String description, int epicId){
        Epic epic = epics.get(epicId);
        SubTask newSubTask = new SubTask(counter, name, description, epic);
        epic.addSubTask(counter, newSubTask);
        counter++;
    }

    public Task getTaskById(int id){
        return tasks.get(id);
    }

    public Epic getEpicById(int id){
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id){
        SubTask subTask = null;
        for (Epic epic : epics.values()) {
            if(epic.getSubTaskById(id) != null)
                subTask = epic.getSubTaskById(id);
        }
        return subTask;
    }

    public void clearTasks(){
        tasks.clear();
    }

    public void clearEpics(){epics.clear();}

    public void removeTask(int taskId){
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId){
            epics.remove(epicId);
    }

    public void removeSubTask(int subTaskId){
        SubTask subTask = getSubTaskById(subTaskId);
        if(subTask != null) {
            subTask.getSubTaskEpic().removeSubTask(subTaskId);
        }
    }

    public void clearEpicSubTasks(int epicId){
        if(epics.containsKey(epicId))
            epics.get(epicId).clearSubTasks();
    }

    public void printAllTasks(){
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    public void printAllEpics(){
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }
    }

    public void updateTask(Task newTask){
        tasks.replace(newTask.getId(), newTask);
    }

    public void updateSubTask(SubTask newSubTask){
        Epic epic = newSubTask.getSubTaskEpic();
        epic.updateSubTask(newSubTask);
    }


}
