package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.SubTask;

import java.io.InputStreamReader;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        TaskManager manager = Managers.getDefault();
        Gson json = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        switch (exchange.getRequestMethod().toUpperCase()) {
            case "GET": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length > 3) {
                    if (manager.getEpicById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Эпик " + path[2] + " не найден.");
                    else {
                        List<SubTask> subTasks = manager.getSubTaskByEpic(Integer.parseInt(path[2]));
                        String result = "";
                        if (!subTasks.isEmpty())
                            result = json.toJson(subTasks);
                        super.sendText(exchange, result);
                    }
                } else if (path.length == 3) {
                    if (manager.getEpicById(Integer.parseInt(path[2])).isEmpty())
                        super.sendNotFound(exchange, "Эпик " + path[2] + " не найден.");
                    else {
                        Epic epic = manager.getEpicById(Integer.parseInt(path[2])).get();
                        String result = json.toJson(epic);
                        super.sendText(exchange, result);
                    }
                } else {
                    List<Epic> epics = manager.getAllEpics();
                    String result = "";
                    if (!epics.isEmpty())
                        result = json.toJson(epics);
                    super.sendText(exchange, result);
                }
                return;
            }
            case "POST": {
                Epic newEpic = json.fromJson(new JsonReader(new InputStreamReader(exchange.getRequestBody())), Epic.class);
                manager.addEpic(newEpic);
                super.sendOk(exchange);
            }
            case "DELETE": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                manager.removeEpic(Integer.parseInt(path[2]));
                super.sendText(exchange, "Эпик удален");
            }
        }
    }
}
