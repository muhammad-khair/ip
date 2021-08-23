package service;

import exception.DukeException;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * TaskManager class.
 *
 * This class acts as the manager of tasks held by the Duke.
 */
public class TaskList {

    // Error message formats.
    private static final String INVALID_TASK_ERROR_MESSAGE = "Task number '%d' is invalid.";
    private static final String FULL_CAPACITY_ERROR_MESSAGE = "Unable to execute as list is full.";
    private static final String EMPTY_LIST_ERROR_MESSAGE = "Unable to execute as list is empty.";

    // Output message formats.
    private static final String EMPTY_LIST_MESSAGE = "List is empty, try adding some tasks first.";
    private static final String TASK_DONE_MESSAGE = "Nice! I've marked this task as done:\n\t%s";
    private static final String TASK_LIST_CONTENTS = "Here are the task(s) in your list:";
    private static final String TASK_DELETED_MESSAGE =
            "Noted. I've removed this task:\n\t%s\nNow you have %d task(s) in the list.";
    private static final String CREATE_TODO_MESSAGE =
            "Got it. I've added this TODO task:\n\t%s\nNow you have %d task(s) in the list.";
    private static final String CREATE_EVENT_MESSAGE =
            "Got it. I've added this EVENT task:\n\t%s\nNow you have %d task(s) in the list.";
    private static final String CREATE_DEADLINE_MESSAGE =
            "Got it. I've added this DEADLINE task:\n\t%s\nNow you have %d task(s) in the list.";

    // Saved task directory.
    private final String TASK_DIRECTORY_PATH = "data";
    private final String TASK_FILE_PATH = TASK_DIRECTORY_PATH + "/duke.txt";
    
    private final static int MAX_STORAGE = 100;
      
    private final List<Task> taskList = new ArrayList<>();
    
    public void loadTasks() {
        try {
            File file = loadAndCreateTaskFile();
            List<Task> savedTaskList = extractTasksFromFile(file);
            loadToList(savedTaskList);
            
        } catch (Exception exception) {
            System.out.printf("Unable to generate/read save data file ./%s\n", TASK_FILE_PATH);
            exception.printStackTrace();
        }
    }
    
    public File loadAndCreateTaskFile() throws DukeException, IOException {
        File directory = new File(TASK_DIRECTORY_PATH);
        File file = new File(TASK_FILE_PATH);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new DukeException(String.format(
                        "Load/save directory ./%s cannot be created.",
                        TASK_DIRECTORY_PATH));
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new DukeException(String.format(
                        "Load/save file ./%s cannot be created.",
                        TASK_FILE_PATH));
            }
        }
        return file;
    }
    
    public List<Task> extractTasksFromFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<Task> savedTaskList = new ArrayList<>();
        
        while (scanner.hasNext()) {
            String taskAsString = scanner.nextLine();
            String splitRegex = " \\" + Task.SPLIT_CHAR + ' '; // split based on regex '|'
            String[] taskAsArray = taskAsString.split(splitRegex);
            boolean isDone = taskAsArray[1].equals("1"); // if not 1, just set to false
            String desc = taskAsArray[2];
            LocalDate date;
            LocalTime time;

            switch (taskAsArray[0]) {
                case Todo.KEYWORD:
                    savedTaskList.add(new Todo(desc, isDone));
                    break;
                case Event.KEYWORD:
                    date = LocalDate.parse(taskAsArray[3]);
                    time = (taskAsArray.length == 4) ? null : LocalTime.parse(taskAsArray[4]);
                    savedTaskList.add(new Event(desc, isDone, date, time));
                    break;
                case Deadline.KEYWORD:
                    date = LocalDate.parse(taskAsArray[3]);
                    time = (taskAsArray.length == 4) ? null : LocalTime.parse(taskAsArray[4]);
                    savedTaskList.add(new Deadline(desc, isDone, date, time));
                    break;
                default:
                    System.out.printf("'%s' is an invalid entry.\n", taskAsString);
            }
        }
        return savedTaskList;
    }

    public void loadToList(List<Task> taskList) throws DukeException {
        if (taskList.size() > MAX_STORAGE) {
            throw new DukeException(FULL_CAPACITY_ERROR_MESSAGE);
        }
        this.taskList.addAll(taskList);
    }
    
    public void saveToFile(Task newTask) throws IOException {
        FileWriter fileWriter = new FileWriter(TASK_FILE_PATH, true);
        fileWriter.write(newTask.toEncodedString() + '\n');
        fileWriter.close();
    }

    public void updateToFile(int taskNumber, Task newTask) throws IOException {
        Path filePath = Paths.get(TASK_FILE_PATH);
        List<String> taskLines = Files.readAllLines(filePath);
        taskLines.set(taskNumber - 1, newTask.toEncodedString()); // 0-indexing
        Files.write(filePath, taskLines);
    }

    public void removeFromFile(int taskNumber) throws IOException {
        Path filePath = Paths.get(TASK_FILE_PATH);
        List<String> taskLines = Files.readAllLines(filePath);
        taskLines.remove(taskNumber - 1); // 0-indexing
        Files.write(filePath, taskLines);
    }

    /**
     * Gets the current number of tasks stored.
     *
     * @return number of tasks stored currently
     */
    public int getTaskListSize() {
        return taskList.size();
    }

    /**
     * Adds a task to the task list. Returns saved task.
     *
     * @param newTask task to save
     * @return saved task
     * @throws DukeException if task cannot be saved, due to full capacity of task list
     */
    public Task addTask(Task newTask) throws DukeException {
        if (taskList.size() == MAX_STORAGE) {
            throw new DukeException(FULL_CAPACITY_ERROR_MESSAGE);
        }
        try {
            saveToFile(newTask); // save to file before saving to collection
            taskList.add(newTask);
        } catch (IOException exception) {
            throw new DukeException("Unable to save task to memory.");
        }
        return newTask;
    }

    /**
     * Adds a Todo Task, returns an output message.
     *
     * @param desc user input with parameters for a Todo
     * @return String message of successful creation of Todo
     * @throws DukeException if task list is full
     */
    public String addToDoTask(String desc) throws DukeException {
        Todo todo = new Todo(desc);
        Task task = addTask(todo);
        return String.format(CREATE_TODO_MESSAGE, task, getTaskListSize());
    }

    /**
     * Adds an Event Task, returns an output message.
     *
     * @param desc todo
     * @return String message of successful creation of Event
     * @throws DukeException if task list is full
     */
    public String addEventTask(String desc, LocalDate atDate, LocalTime atTime) 
            throws DukeException {     
        
        Event event = new Event(desc, atDate, atTime);
        Task task = addTask(event);
        return String.format(CREATE_EVENT_MESSAGE, task, getTaskListSize());
    }

    /**
     * Adds a Deadline Task, returns an output message.
     *
     * @param desc todo user input with parameters for a Deadline
     * @return String message of successful creation of Deadline
     * @throws DukeException if task list is full
     */
    public String addDeadlineTask(String desc, LocalDate byDate, LocalTime byTime)
            throws DukeException {

        Deadline deadline = new Deadline(desc, byDate, byTime);
        Task task = addTask(deadline);
        return String.format(CREATE_DEADLINE_MESSAGE, task, getTaskListSize());
    }

    /**
     * Update a Task completed based on the input Task number fed as a String.
     * Provides an output message on return.
     *
     * @param taskNumber String format of the Task number to delete
     * @return String message of successful completion marking of Task
     * @throws DukeException if the Task number is not valid
     */
    public String updateTaskAsDone(int taskNumber) throws DukeException {
        Task selectedTask = getTaskFromNumberString(taskNumber);
        boolean wasDone = selectedTask.isDone();
        try {
            selectedTask.markAsDone();
            updateToFile(taskNumber, selectedTask);
            
        } catch (IOException exception) {
            selectedTask.setDone(wasDone); // set it back to old value if fails
            throw new DukeException(String.format(
                    "Unable to update task numbered %d to file.", 
                    taskNumber));
        }
        return String.format(TASK_DONE_MESSAGE, selectedTask);
    }

    /**
     * Deletes a Task based on the input Task number fed as a String.
     * Provides an output message on return.
     *
     * @param taskNumber String format of the Task number to delete
     * @return String message of successful deletion of Task
     * @throws DukeException if the Task number is not valid
     */
    public String deleteTask(int taskNumber) throws DukeException {
        Task selectedTask = getTaskFromNumberString(taskNumber);
        try {
            removeFromFile(taskNumber);
            taskList.remove(selectedTask); // remove shifts tasks to the right backwards
            
        } catch (IOException exception) {
            throw new DukeException(String.format(
                    "Unable to delete task numbered %d to file.",
                    taskNumber));
        }
        return String.format(TASK_DELETED_MESSAGE, selectedTask, getTaskListSize());
    }

    /**
     * Gets the Task based on the input Task number fed as a String.
     *
     * @param taskNumber String format of the Task number to obtain
     * @return associated Task
     * @throws DukeException if the Task List is empty or the Task number is not valid
     */
    public Task getTaskFromNumberString(int taskNumber) throws DukeException {
        if (taskList.isEmpty()) {
            throw new DukeException(EMPTY_LIST_ERROR_MESSAGE);
        }
        if (taskNumber <= 0 || taskList.size() < taskNumber) {
            throw new DukeException(String.format(INVALID_TASK_ERROR_MESSAGE, taskNumber));
        }
        return taskList.get(taskNumber - 1); // shift to 0-indexing
    }

    /**
     * Provides the Tasks in a formatted String, to be fed to a text printer.
     *
     * @return formatted tasks in String
     */
    public String getTaskList() {
        if (taskList.isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }
        StringBuilder tasksAsString = new StringBuilder(TASK_LIST_CONTENTS);
        for (int idx = 0; idx < taskList.size(); idx ++) {
            tasksAsString.append(String.format("\n\t%d. %s", idx + 1, taskList.get(idx)));
        }
        return tasksAsString.toString();
    }
}