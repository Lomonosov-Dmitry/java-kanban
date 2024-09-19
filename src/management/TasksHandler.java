package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.InputStreamReader;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        TaskManager manager = Managers.getDefault();
        switch (exchange.getRequestMethod().toUpperCase()) {
            case "GET": {
                Gson json = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .create();
                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length > 2) {
                    if (manager.getTaskById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Задача " + path[2] + " не найдена.");
                    else {
                        Task task = manager.getTaskById(Integer.parseInt(path[2])).get();
                        String result = json.toJson(task);
                        super.sendText(exchange, result);
                    }
                } else {
                    List<Task> tasks = manager.getAllTasks();
                    String result = "";
                    if (!tasks.isEmpty())
                        result = json.toJson(tasks);
                    super.sendText(exchange, result);
                }
                return;
            }
            case "POST": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                Gson json = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskAdapter())
                        .create();
                if (path.length > 2) {
                    if (manager.getTaskById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Задача " + path[2] + " не найдена.");
                    else {
                        Task newTask = json.fromJson(new JsonReader(new InputStreamReader(exchange.getRequestBody())), Task.class);
                        if (newTask.getStartTime() == null || manager.timeValidator(newTask)) {
                            manager.updateTask(newTask);
                            super.sendOk(exchange);
                        } else
                            super.sendHasInteractions(exchange, "Уазанное время уже занято");
                    }
                } else {
                    Task newTask = json.fromJson(new JsonReader(new InputStreamReader(exchange.getRequestBody())), Task.class);
                    if (newTask.getStartTime() == null || manager.timeValidator(newTask)) {
                        manager.addTask(newTask);
                        super.sendOk(exchange);
                    } else
                        super.sendHasInteractions(exchange, "Уазанное время уже занято");
                }
                return;
            }
            case "DELETE": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                manager.removeTask(Integer.parseInt(path[2]));
                super.sendText(exchange, "Задача удалена");
            }
        }
    }
}