package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void subTasksShouldBeEqual(){
        SubTask firstSub = new SubTask(1, "aaa", "ddd", TaskStatus.NEW, 54);
        SubTask secondSub = new SubTask(1, "ddd", "eeee", TaskStatus.NEW, 58);

        assertEquals(firstSub, secondSub, "Подзадачи не одинаковы!");
    }
}