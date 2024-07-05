import Management.TaskManager;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import Model.TaskStatus;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Поехали!");
        while (true) {
            printMainMenu();
            int command = scanner.nextInt();
            switch (command) {
                case 1: {
                    printAddMenu();
                    Add(scanner, manager);
                    break;
                }
                case 2: {
                    printShowMenu();
                    Show(scanner, manager);
                    break;
                }
                case 3: {
                    printUpdateMenu();
                    Update(scanner, manager);
                    break;
                }
                case 0: {
                    System.out.println("Приехали, конечная! Просьба всем выйти из вагонов!");
                    return;
                }
                default: {
                    System.out.println("Такой команды не знаю, попробуйте еще раз");
                }
            }

        }
    }

    public static void printMainMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - Добавить ...");
        System.out.println("2 - Показать ...");
        System.out.println("3 - Обновить/очистить");
        System.out.println("0 - Выход");
    }


    public static void printAddMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Добавить эпик");
        System.out.println("3 - Добавить подзадачу");
        System.out.println("0 - Вернуться");
    }

    public static void printShowMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - Показать список задач");
        System.out.println("2 - Показать список эпиков");
        System.out.println("0 - Вернуться");
    }

    public static void printUpdateMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 - Обновить задачу");
        System.out.println("2 - Обновить подзадачу");
        System.out.println("3 - Очистить список задач");
        System.out.println("4 - Очистить список подзадач");
        System.out.println("5 - Очистить список эпиков");
        System.out.println("0 - Вернуться");
    }

    public static void Add(Scanner scanner, TaskManager manager) {
        int command = scanner.nextInt();
        switch (command) {
            case 1: {
                System.out.println("Введите название задачи:");
                String name = scanner.next();
                System.out.println("Введите описание задачи:");
                String description = scanner.next();
                System.out.println("Введите статус задачи:");
                String status = scanner.next().toUpperCase();
                Task newTask = new Task(name, description, getStatusFromString(status));
                manager.addTask(newTask);
                break;
            }
            case 2: {
                System.out.println("Введите название эпика:");
                String name = scanner.next();
                System.out.println("Введите описание эпика:");
                String description = scanner.next();
                Epic newEpic = new Epic(name, description);
                manager.addEpic(newEpic);
                break;
            }
            case 3: {
                manager.getAllEpics().toString();
                System.out.println("Введите ID эпика для подзадачи");
                int epicId = scanner.nextInt();
                System.out.println("Введите название подзадачи:");
                String name = scanner.next();
                System.out.println("Введите описание подзадачи:");
                String description = scanner.next();
                System.out.println("Введите статус подзадачи:");
                String status = scanner.next().toUpperCase();
                SubTask newSubtask = new SubTask(name, description, getStatusFromString(status), epicId);
                manager.addSubTask(newSubtask);
                break;
            }
            case 0: {
                break;
            }
            default: {
                System.out.println("Такой команды не знаю, попробуйте еще раз");
            }
        }
    }

    public static void Show(Scanner scanner, TaskManager manager) {
        int command = scanner.nextInt();
        switch (command) {
            case 1: {
                manager.getAllTasks().toString();
                break;
            }
            case 2: {
                manager.getAllEpics().toString();
                break;
            }
            case 0: {
                break;
            }
            default: {
                System.out.println("Такой команды не знаю, попробуйте еще раз");
            }
        }
    }

    public static void Update(Scanner scanner, TaskManager manager) {
        int command = scanner.nextInt();
        switch (command) {
            case 1: {
                System.out.println("Введите номер задачи:");
                int taskId = scanner.nextInt();
                System.out.println("Введите новый статус для задачи:");
                String status = scanner.next().toUpperCase();

                Task oldTask = manager.getTaskById(taskId);
                if(oldTask == null){
                    System.out.println("Задача с таким номером не найдена!");
                    break;
                }
                Task newTask = new Task(taskId, oldTask.getName(), oldTask.getDescription(), getStatusFromString(status));
                manager.updateTask(newTask);
                break;
            }
            case 2: {
                System.out.println("Введите номер подзадачи:");
                int subTaskId = scanner.nextInt();
                System.out.println("Введите новый статус для подзадачи:");
                String status = scanner.next().toUpperCase();

                SubTask oldSubTask = manager.getSubTaskById(subTaskId);
                if(oldSubTask == null){
                    System.out.println("Подзадача с таким номером не найдена!");
                    break;
                }
                SubTask newSubTask = new SubTask(subTaskId, oldSubTask.getName(), oldSubTask.getDescription(),
                        getStatusFromString(status), oldSubTask.getEpicId());
                manager.updateSubTask(newSubTask);
                break;
            }
            case 3: {
                manager.clearTasks();
                break;
            }
            case 4: {
                System.out.println("Введите номер эпика, у которого очищаем подзадачи:");
                int epicId = scanner.nextInt();
                manager.clearEpicSubTasks(epicId);
                break;
            }
            case 5: {
                manager.clearEpics();
                break;
            }
            case 0: {
                break;
            }
            default: {
                System.out.println("Такой команды не знаю, попробуйте еще раз");
            }
        }
    }

    public static TaskStatus getStatusFromString(String status){
        TaskStatus newStatus = null;
        switch (status) {
            case "NEW": {
                newStatus = TaskStatus.NEW;
                break;
            }
            case "IN_PROGRESS": {
                newStatus = TaskStatus.IN_PROGRESS;
                break;
            }
            case "DONE": {
                newStatus = TaskStatus.DONE;
                break;
            }
            default: {
                System.out.println("Не знаю такого статуса");
                break;
            }
        }
        return newStatus;
    }

}

