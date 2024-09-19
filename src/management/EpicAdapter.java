package management;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Epic;
import model.SubTask;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EpicAdapter extends TypeAdapter<Epic> {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) {
        try {
            jsonWriter.beginObject();
            jsonWriter.name("id").value(epic.getId());
            jsonWriter.name("name").value(epic.getName());
            jsonWriter.name("description").value(epic.getDescription());
            jsonWriter.name("status").value(epic.getStatus().toString());
            if (!epic.getSubTasks().isEmpty()) {
                jsonWriter.name("subtasks");
                writeSubTasksArray(jsonWriter, Managers.getDefault().getSubTaskByEpic(epic.getId()));
            }
            if (epic.getEndTime() != null) {
                jsonWriter.name("startTime").value(epic.getStartTime().format(dateTimeFormatter));
                jsonWriter.name("duration").value(epic.getDuration().toMinutes());
                jsonWriter.name("endTime").value(epic.getEndTime().format(dateTimeFormatter));
            }
            jsonWriter.endObject();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания json из эпика: " + e.getMessage());
        }

    }

    @Override
    public Epic read(JsonReader jsonReader) {
        try {
            String name = null;
            String description = null;
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                switch (jsonReader.nextName()) {
                    case "name": {
                        name = jsonReader.nextString();
                        break;
                    }
                    case "description": {
                        description = jsonReader.nextString();
                        break;
                    }
                    default:
                        jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.close();
            return new Epic(name, description);
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания эпика из json: " + e.getMessage());
            return null;
        }
    }

    public void writeSubTasksArray(JsonWriter jsonWriter, List<SubTask> subTasks) {
        try {
            jsonWriter.beginArray();
            for (SubTask subTask : subTasks) {
                writeSubTask(jsonWriter, subTask);
            }
            jsonWriter.endArray();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка создания массива подзадач в эпике: " + e.getMessage());
        }
    }

    public void writeSubTask(JsonWriter jsonWriter, SubTask subTask) {
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
            Managers.getDefault().handleIOException("Ошибка создания подзадачи в эпике: " + e.getMessage());
        }
    }

}
