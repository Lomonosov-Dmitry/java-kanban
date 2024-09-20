import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import management.EpicAdapter;
import management.Managers;
import management.SubTaskAdapter;
import management.TaskAdapter;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    Managers managers = new Managers();
    HttpTaskServer taskserver = new HttpTaskServer();
    Gson json = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .create();

    public HttpTaskServerTest() {
    }

    @BeforeEach
    public void setUp() {
        Managers.getDefault().clearTasks();
        Managers.getDefault().clearEpics();
        taskserver.serverStart();
    }

    @AfterEach
    public void shutDown() {
        taskserver.serverStop();
    }

    @Test
    public void taskHandlerTest() throws IOException, InterruptedException {
        Task task = new Task("Task1", "Task1Des", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        String taskJson = json.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> allTasks = Managers.getDefault().getAllTasks();

        assertNotNull(allTasks, "Задача не создана!");
        assertEquals(1, allTasks.size(), "Количество задач не совпадает!");
        assertEquals("Task1", allTasks.getFirst().getName(), "Название задачи не совпадает!");

        task = allTasks.getFirst();
        task.setName("Task11");
        url = URI.create("http://localhost:8080/tasks/1");
        taskJson = json.toJson(task);
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        allTasks = Managers.getDefault().getAllTasks();
        assertEquals(1, allTasks.size(), "Количество задач не совпадает!");
        assertEquals("Task11", allTasks.getFirst().getName(), "Название задачи не совпадает!");

        url = URI.create("http://localhost:8080/tasks/10");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<InputStream> streamResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        assertEquals(200, streamResponse.statusCode());

        Task newTask = json.fromJson(new JsonReader(new InputStreamReader(streamResponse.body())), Task.class);
        assertEquals(task, newTask, "Задачи не совпадают!");

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        allTasks = Managers.getDefault().getAllTasks();
        assertEquals(0, allTasks.size(), "Задача не удалена!");
    }

    @Test
    public void epicHandlerTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Epic1Des");
        String epicJson = json.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> allEpics = Managers.getDefault().getAllEpics();
        assertNotNull(allEpics, "Эпик не создана!");
        assertEquals(1, allEpics.size(), "Количество эпиков не совпадает!");
        assertEquals("Epic1", allEpics.getFirst().getName(), "Название эпика не совпадает!");

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/epics/10");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        url = URI.create("http://localhost:8080/epics/10/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        url = URI.create("http://localhost:8080/epics/1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/epics/1/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/epics/1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        allEpics = Managers.getDefault().getAllEpics();
        assertEquals(0, allEpics.size(), "Эпик не удален!");
    }

    @Test
    public void subTaskHandlerTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Epic1Des");
        SubTask subTask = new SubTask("SubTask1", "SubTask1Des", TaskStatus.NEW, 1, LocalDateTime.now(), Duration.ofMinutes(30));
        Managers.getDefault().addEpic(epic);;
        String subTaskJson = json.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> allSubTasks = Managers.getDefault().getAllSubTasks();
        assertNotNull(allSubTasks, "Подзадача не создана!");
        assertEquals(1, allSubTasks.size(), "Количество подзадач не совпадает!");
        assertEquals("SubTask1", allSubTasks.getFirst().getName(), "Название подзадачи не совпадает!");

        subTask.setName("SubTask11");
        subTask = Managers.getDefault().getAllSubTasks().getFirst();
        subTaskJson = json.toJson(subTask);
        url = URI.create("http://localhost:8080/subtasks/2");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        subTask = new SubTask("SubTask1", "SubTask1Des", TaskStatus.NEW, 1, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(30));
        subTaskJson = json.toJson(subTask);
        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks/20");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks/2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks/2");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        allSubTasks = Managers.getDefault().getAllSubTasks();
        assertEquals(0, allSubTasks.size(), "Подзадача не удалена!");
    }

    @Test
    public void historyHandlerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void prioritizedHandlerTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}