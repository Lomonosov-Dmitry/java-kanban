public class SubTask extends Task {

    private final Epic epic;


    protected SubTask(int id, String name, String description, Epic epic) {
        super(id, name, description);
        this.epic = epic;
        this.setStatus(TaskStatus.NEW);
    }

    protected int getEpicId(){
        return epic.getId();
    }

    protected Epic getSubTaskEpic(){
        return epic;
    }

    @Override
    protected void setStatus(TaskStatus status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", epic=" + epic.getId() +
                '}';
    }
}
