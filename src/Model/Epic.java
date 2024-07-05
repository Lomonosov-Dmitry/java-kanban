package Model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(int counter) {
        subTasks.add(counter);
    }

    public void removeSubTask(int id) {
        subTasks.remove(Integer.valueOf(id));
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    @Override
    public String toString() {
        return "Model.Epic{" +
                "id=" + this.getId() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                "subTasks=" + subTasks.toString() +
                '}';
    }

}
