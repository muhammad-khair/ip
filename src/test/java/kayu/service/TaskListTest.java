package kayu.service;

import static kayu.service.TaskList.EMPTY_LIST_ERROR_MESSAGE;
import static kayu.service.TaskList.FULL_CAPACITY_ERROR_MESSAGE;
import static kayu.service.TaskList.INVALID_TASK_ERROR_MESSAGE;
import static kayu.service.TaskList.MAX_STORAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kayu.exception.KayuException;
import kayu.exception.StorageException;
import kayu.task.Task;
import kayu.task.Todo;


public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() throws StorageException {
        taskList = new TaskList();
        List<Task> tasks = IntStream.rangeClosed(1, 10)
                .boxed()
                .map(num -> new Todo("mock " + num))
                .collect(Collectors.toList());
        taskList.initializeTasks(tasks);
    }

    @Test
    public void testGetCapacity() {
        assertEquals(10, taskList.getCapacity());
    }

    @Test
    public void testAddTask() {
        try {
            int prevSize = taskList.getCapacity();
            Task newTask = new Todo("new todo here");
            taskList.addTask(newTask);
            assertEquals(prevSize + 1, taskList.getCapacity());

        } catch (KayuException exception) {
            System.out.println(exception.getMessage());
            fail();
        }
    }
    
    @Test
    public void testDeleteValidTask() {
        int taskNumber = 5;
        try {
            String expectedDesc = "mock " + taskNumber;
            Task deletedTask = taskList.deleteTask(taskNumber);
            assertEquals(expectedDesc, deletedTask.getDescription());
            
        } catch (KayuException exception) {
            System.out.println(exception.getMessage());
            fail();
        }
    }

    @Test
    public void testMarkTaskAsDone() {
        int taskNumber = 8;
        try {
            Task updatedTask = taskList.updateTaskAsDone(taskNumber);
            assertTrue(updatedTask.isDone());
            
        } catch (KayuException exception) {
            System.out.println(exception.getMessage());
            fail();
        }
    }

    @Test
    public void addTask_maxCapacity_exceptionThrown() throws StorageException {
        List<Task> tasks = IntStream.rangeClosed(1, MAX_STORAGE)
                .boxed()
                .map(num -> new Todo("mock " + num))
                .collect(Collectors.toList());
        taskList.initializeTasks(tasks);
        
        try {
            Task newTask = new Todo("new todo here");
            taskList.addTask(newTask);
            fail();

        } catch (KayuException exception) {
            assertEquals(FULL_CAPACITY_ERROR_MESSAGE, exception.getMessage());
        }
    }

    @Test
    public void deleteTask_taskNumberIsInvalid_exceptionThrown() {
        int taskNumber = 12;
        try {
            taskList.deleteTask(taskNumber);
            fail();

        } catch (KayuException exception) {
            String expected = String.format(INVALID_TASK_ERROR_MESSAGE, taskNumber);
            assertEquals(expected, exception.getMessage());
        }
    }

    @Test
    public void deleteTask_taskListIsEmpty_exceptionThrown() throws StorageException {
        int taskNumber = 12;
        taskList.initializeTasks(new ArrayList<>());
        try {
            taskList.deleteTask(taskNumber);
            fail();

        } catch (KayuException exception) {
            assertEquals(EMPTY_LIST_ERROR_MESSAGE, exception.getMessage());
        }
    }
}
