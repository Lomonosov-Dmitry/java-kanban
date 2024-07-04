public class SubTask extends Task {

    private final int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    protected int getEpicId(){
        return epicId;
    }

//    @Override
//    protected void setStatus(TaskStatus status) {
//        super.setStatus(status);
//        epic.updateStatus();
//    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", epic=" + epicId +
                '}';
    }
}
