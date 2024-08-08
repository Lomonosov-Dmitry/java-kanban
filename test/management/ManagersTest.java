package management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    Managers managers = new Managers();

    @Test
    void inMemoryManagersShouldPresent(){
        assertNotNull(managers.getDefault(), "Отсутствует InMemoryTaskManager!");
        assertNotNull(managers.getDefaultHistory(), "Отсутствует InMemoryHistoryManager!");
    }
}