package management;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            List<Task> history = Managers.getDefaultHistory().getHistory();
            Gson json = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                    .registerTypeAdapter(Epic.class, new EpicAdapter())
                    .create();
            byte[] response = "".getBytes();
            if (!history.isEmpty())
                response = json.toJson(history).getBytes();
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка обработки истории просмотров: " + e.getMessage());
        }
    }
}
