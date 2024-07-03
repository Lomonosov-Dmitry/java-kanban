import java.util.HashMap;

public class Epic extends Task {

    private final HashMap<Integer, SubTask> subTasks;

    protected Epic(int id, String name, String description) {
        super(id, name, description);
        this.subTasks = new HashMap<>();
        this.setStatus(TaskStatus.NEW);
    }

    protected void addSubTask(int id, SubTask subTask) {
        subTasks.put(id, subTask);
        updateStatus();
    }

    protected void removeSubTask(int id) {
        subTasks.remove(id);
        updateStatus();
    }

    protected void clearSubTasks() {
        subTasks.clear();
        this.setStatus(TaskStatus.DONE);
    }

    protected void updateStatus() {
        int doneSubs = 0;
        int newSubs = 0;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                this.setStatus(TaskStatus.IN_PROGRESS);
                break;
            } else if (subTask.getStatus().equals(TaskStatus.NEW)) {
                newSubs++;
            } else
                doneSubs++;
        }
        if (doneSubs == subTasks.size())
            this.setStatus(TaskStatus.DONE);
        else if (newSubs == subTasks.size())
            this.setStatus(TaskStatus.NEW);
        else
            this.setStatus(TaskStatus.IN_PROGRESS);

    }

    protected SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    protected void updateSubTask(SubTask newSubTask) {
        subTasks.replace(newSubTask.getId(), newSubTask);
        updateStatus();
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
