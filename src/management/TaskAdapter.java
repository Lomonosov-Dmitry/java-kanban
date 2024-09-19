package management;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Task;
import model.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");
    @Override
    public void write(JsonWriter jsonWriter, Task task) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("id").value(task.getId());
            jsonWriter.name("name").value(task.getName());
            jsonWriter.name("description").value(task.getDescription());
            jsonWriter.name("status").value(task.getStatus().toString());
            if (task.getEndTime() != null) {
                jsonWriter.name("startTime").value(task.getStartTime().format(dateTimeFormatter));
                jsonWriter.name("duration").value(task.getDuration().toMinutes());
            }
            jsonWriter.endObject();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания json из задачи: " + e.getMessage());
        }
    }

    @Override
    public Task read(JsonReader jsonReader) {
        int id = -1;
        String name = null;
        String description = null;
        TaskStatus status = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                switch (jsonReader.nextName()) {
                    case "id": {
                        id = jsonReader.nextInt();
                        break;
                    }
                    case "name": {
                        name = jsonReader.nextString();
                        break;
                    }
                    case "description": {
                        description = jsonReader.nextString();
                        break;
                    }
                    case "status": {
                        status = TaskStatus.valueOf(jsonReader.nextString().toUpperCase());
                        break;
                    }
                    case "startTime": {
                        startTime = LocalDateTime.parse(jsonReader.nextString(), dateTimeFormatter);
                        break;
                    }
                    case "duration": {
                        duration = Duration.ofMinutes(jsonReader.nextLong());
                        break;
                    }
                }
            }
            jsonReader.endObject();
            jsonReader.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания задачи из json: " + e.getMessage());
        }
        Task newTask = null;
        if (id == -1 && startTime == null)
            newTask = new Task(name, description, status);
        else if (id == -1)
            newTask = new Task(name, description, status, startTime, duration);
        else
            newTask = new Task(id, name, description, status, startTime, duration);
        return newTask;
    }
}
