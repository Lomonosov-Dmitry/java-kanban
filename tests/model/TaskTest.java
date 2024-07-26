package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksShouldBeEqual(){
        Task firstTask = new Task(1, "aaa", "bbb", TaskStatus.NEW);
        Task secondTask = new Task(1, "bbb", "aaa", TaskStatus.DONE);

        assertEquals(firstTask, secondTask, "Задачи не одинаковы!");
    }
}