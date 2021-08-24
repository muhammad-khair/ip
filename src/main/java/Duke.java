import commands.Command;
import exception.DukeException;
import exception.StorageException;
import service.ChatBot;
import parser.Parser;
import service.TaskList;
import storage.Storage;
import task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Duke class.
 *
 * This class holds the main logic for Duke project (CS2103T's iP).
 * Duke is a task manager for users that love the command line interface.
 * @author muhammad-khair, damithc
 */
public class Duke {

    private final Parser parser = new Parser();

    private final TaskList taskList = new TaskList();
    
    private final Storage storage = new Storage();

    private final ChatBot chatBot = new ChatBot();
    
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Driver function for main logic.
     *
     * @param args command line arguments fed
     */
    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.run();
        System.exit(0);
    }

    /**
     * Runs the whole Duke process.
     *
     * Consists of (1) scanning input and parsing it to a
     * Command, (2) parsing the inputs in the format required by the Command and (3)
     * executing the Commanding and returning a String output to be printed to console.
     */
    public void run() {
        chatBot.logo();
        init();
        chatBot.greet();
        readCommandsUntilBye();
        chatBot.exit();
    }
    
    public void init() {
        try {
            List<Task> tasks = storage.load();
            taskList.init(tasks);
            
        } catch (StorageException exception) {
            chatBot.error(exception.getMessage());
            errorExit();
        }
    }
    
    public void readCommandsUntilBye() {
        Command command;
        do {
            String userInput = scanner.nextLine().trim();
            command = parser.parseToCommand(userInput);
            try {
                String feedback = command.execute(taskList);
                List<Task> tasks = taskList.getTasks();
                storage.save(tasks);
                chatBot.info(feedback);

            } catch (DukeException exception) {
                chatBot.error(exception.getMessage());

            } catch (StorageException exception) {
                chatBot.errorOnSave();
                errorExit();
            }
        } while (!command.isBye());
        scanner.close();
    }
    
    public void errorExit() {
        chatBot.exitOnError();
        scanner.close();
        System.exit(1); // exit with error status
    }
}
