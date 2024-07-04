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

    protected ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    protected void addSubTask(int counter) {
        subTasks.add(counter);
    }

    protected void removeSubTask(int id) {
        subTasks.remove(id);
    }

    protected void clearSubTasks() {
        subTasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                "subTasks=" + subTasks.toString() +
                '}';
    }

}
