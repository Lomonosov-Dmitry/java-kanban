package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
        this.subTasks = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    @Override
    public String toCSV() {
        String finalString = String.format("%d,EPIC,%s,%s,%s,", this.getId(), this.getName(), this.getStatus(), this.getDescription());
        if (this.getStartTime() != null && this.getDuration() != null)
                finalString += "," + this.getStartTime().format(dateTimeFormatter) + "," + this.getDuration().toMinutes();
        return finalString;
    }

}
