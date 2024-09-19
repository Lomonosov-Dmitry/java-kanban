package management;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.SubTask;
import model.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTaskAdapter extends TypeAdapter<SubTask> {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("id").value(subTask.getId());
            jsonWriter.name("name").value(subTask.getName());
            jsonWriter.name("description").value(subTask.getDescription());
            jsonWriter.name("status").value(subTask.getStatus().toString());
            jsonWriter.name("epicId").value(subTask.getEpicId());
            if (subTask.getEndTime() != null) {
                jsonWriter.name("startTime").value(subTask.getStartTime().format(dateTimeFormatter));
                jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
            }
            jsonWriter.endObject();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания json из подзадачи: " + e.getMessage());
        }
    }

    @Override
    public SubTask read(JsonReader jsonReader) {
        int id = -1;
        String name = null;
        String description = null;
        TaskStatus status = null;
        int epicId = -1;
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
                    case "epicId": {
                        epicId = jsonReader.nextInt();
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
            Managers.getDefault().handleIOException("Ошибка создания подзадачи из json: " + e.getMessage());
        }
        SubTask newSubTask = null;
        if (id == -1 && startTime == null)
            newSubTask = new SubTask(name, description, status, epicId);
        else if (id == -1)
            newSubTask = new SubTask(name, description, status, epicId, startTime, duration);
        else
            newSubTask = new SubTask(id, name, description, status, epicId, startTime, duration);
        return newSubTask;
    }
}
