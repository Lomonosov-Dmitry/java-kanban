package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            List<Task> prioritized = Managers.getDefault().getPrioritizedTasks();
            Gson json = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                    .create();
            byte[] response = "".getBytes();
            if (!prioritized.isEmpty())
                response = json.toJson(prioritized).getBytes();
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка обработки расписания: " + e.getMessage());
        }
    }
}
