package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;

import java.io.InputStreamReader;
import java.util.List;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        TaskManager manager = Managers.getDefault();
        switch (exchange.getRequestMethod().toUpperCase()) {
            case "GET": {
                Gson json = new GsonBuilder()
                        .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                        .create();
                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length > 2) {
                    if (manager.getSubTaskById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Задача " + path[2] + " не найдена.");
                    else {
                        SubTask subTask = manager.getSubTaskById(Integer.parseInt(path[2])).get();
                        String result = json.toJson(subTask);
                        super.sendText(exchange, result);
                    }
                } else {
                    List<SubTask> subTasks = manager.getAllSubTasks();
                    String result = "";
                    if (!subTasks.isEmpty())
                        result = json.toJson(subTasks);
                    super.sendText(exchange, result);
                }
                return;
            }
            case "POST": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                Gson json = new GsonBuilder()
                        .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                        .create();
                if (path.length > 2) {
                    if (manager.getSubTaskById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Задача " + path[2] + " не найдена.");
                    else {
                        SubTask newSubTask = json.fromJson(new JsonReader(new InputStreamReader(exchange.getRequestBody())), SubTask.class);
                        if (newSubTask.getStartTime() == null || manager.timeValidator(newSubTask)) {
                            manager.updateSubTask(newSubTask);
                            super.sendOk(exchange);
                        } else
                            super.sendHasInteractions(exchange, "Уазанное время уже занято");
                    }
                } else {
                    SubTask newSubTask = json.fromJson(new JsonReader(new InputStreamReader(exchange.getRequestBody())), SubTask.class);
                    if (newSubTask.getStartTime() == null || manager.timeValidator(newSubTask)) {
                        manager.addSubTask(newSubTask);
                        super.sendOk(exchange);
                    } else
                        super.sendHasInteractions(exchange, "Уазанное время уже занято");
                }
                return;
            }
            case "DELETE": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                manager.removeSubTask(Integer.parseInt(path[2]));
                super.sendText(exchange, "Задача удалена");
            }
        }
    }
}
