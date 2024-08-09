package model;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryLinkedList {
    public Node<Task> tail;
    private final HashMap<Integer, Node<Task>> historyMap;

    public HistoryLinkedList() {
        this.tail = null;
        historyMap = new HashMap<>();
    }

    public <T extends Task> void linkLast(T task) {
        if (historyMap.containsKey(task.getId()))
            removeNode(historyMap.get(task.getId()));
        Node<Task> newNode = new Node<>(task);
        if (tail != null) {
            newNode.prev = tail;
            tail.next = newNode;
        }
        tail = newNode;
        historyMap.put(task.getId(), newNode);
    }

    public void removeTaskById(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            historyMap.remove(node.data.getId());
            if (node.next == null) {
                tail = node.prev;
                node.prev.next = null;
            } else if (node.prev == null) {
                node.next.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (tail != null) {
            Node<Task> node = tail;
            while (node != null) {
                tasks.add(node.data);
                node = node.prev;
            }
        }
        return tasks;
    }
}
