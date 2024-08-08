package management;

import model.Task;
import model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HistoryLinkedList historyLinkedList;

    public InMemoryHistoryManager() {
        this.historyLinkedList = new HistoryLinkedList();
    }

    @Override
    public <T extends Task> void add(T task) {
        historyLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyLinkedList.removeTaskById(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyLinkedList.getTasks();
    }
}

class HistoryLinkedList {
    public Node<Task> head;
    public Node<Task> tail;
    public int size = 0;
    private final HashSet<Node<Task>> historySet;
    private final HashMap<Integer, Node<Task>> historyMap;

    public HistoryLinkedList() {
        this.head = null;
        this.tail = null;
        historySet = new HashSet<>();
        historyMap = new HashMap<>();
    }

    public <T extends Task> void linkLast(T task) {
        if (head == null) {
            Node<Task> newNode = new Node<>(task);
            historySet.add(newNode);
            head = newNode;
            tail = newNode;
            historyMap.put(task.getId(), newNode);
        } else if (head.data.equals(task) && historySet.size() == 1) {
            head.data = task;
            return;
        } else {
            if (historyMap.containsKey(task.getId()))
                removeNode(historyMap.get(task.getId()));
            Node<Task> newNode = new Node<>(task);
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
            historySet.add(newNode);
            historyMap.put(task.getId(), newNode);
        }
        size++;

    }

    public void removeTaskById(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            historyMap.remove(node.data.getId());
            if (node.prev == null && node.next == null) {
                historySet.clear();
                head = null;
                tail = null;
            } else {
                if (node.prev == null) {
                    Node<Task> nextNode = node.next;
                    nextNode.prev = null;
                    head = nextNode;
                } else if (node.next == null) {
                    Node<Task> privNode = node.prev;
                    privNode.next = null;
                    tail = privNode;
                } else {
                    Node<Task> privNode = node.prev;
                    Node<Task> nextNode = node.next;
                    privNode.next = nextNode;
                    nextNode.prev = privNode;
                }
                historySet.remove(node);
                size--;
            }
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (tail != null) {
            Node<Task> node = tail;
            while (node.prev != null) {
                tasks.add(node.data);
                node = node.prev;
            }
            tasks.add(node.data);
        }
        return tasks;
    }
}
