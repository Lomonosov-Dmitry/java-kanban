import com.sun.net.httpserver.HttpServer;
import management.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    Managers managers = null;
    HttpServer server = null;

    public HttpTaskServer() {
        this.managers = new Managers();
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.createContext("/subtasks", new SubTasksHandler());
            httpServer.createContext("/epics", new EpicsHandler());
            httpServer.createContext("/prioritized", new PrioritizedHandler());
            server = httpServer;
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка запуска http-сервера: " + e.getMessage());
        }
    }

    public void serverStart() {
        server.start();
    }

    public void serverStop() {
        server.stop(0);
    }

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.serverStart();
        /*Managers.getDefault().addTask(new Task("Task1", "DesTask1", TaskStatus.NEW, LocalDateTime.now().plusMinutes(180), Duration.ofMinutes(120)));
        Managers.getDefault().addTask(new Task("Task2", "DesTask2", TaskStatus.NEW));
        Managers.getDefault().addEpic(new Epic("Epic1", "DesEpic1"));
        Managers.getDefault().addSubTask(new SubTask("SubTask1", "DesSubTask1", TaskStatus.NEW, 3, LocalDateTime.now(), Duration.ofMinutes(120)));*/

    }
}
