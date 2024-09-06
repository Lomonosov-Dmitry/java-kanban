package model;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public SubTask(String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Model.SubTask{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() + '\'' +
                ", epic=" + epicId +
                '}';
    }

    @Override
    public String toCSV() {
        String finalString = String.format("%d,SUBTASK,%s,%s,%s,%d", this.getId(), this.getName(), this.getStatus(), this.getDescription(), epicId);
        if(this.getStartTime() != null && this.getDuration() != null)
            finalString += "," + this.getStartTime().format(DATE_TIME_FORMATTER) + "," + this.getDuration().toMinutes();
        return finalString;
    }
}
