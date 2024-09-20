package management;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText(HttpExchange exchange, String text) {
        try {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка отправки статуса 200: " + e.getMessage());
        }
    }

    protected void sendOk(HttpExchange exchange, String text) {
        try {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(201, 0);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка отправки статуса 201: " + e.getMessage());
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) {
        try {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(404, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка отправки статуса 404: " + e.getMessage());
        }
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) {
        try {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(406, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        } catch (IOException e) {
            Managers.getDefault().handleIOException("Ошибка отправки статуса 406: " + e.getMessage());
        }
    }

}
