package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicsShouldBeEqual() {
        Epic firstEpic = new Epic(1, "aaaa", "bbb");
        Epic secondEpic = new Epic(1, "ddd", "eee");

        assertEquals(firstEpic, secondEpic, "Эпики не одинаковы!");
    }
}