package kayu.commands;

import static kayu.commands.CommandMessage.ERROR_EMPTY_PARAMS;
import static kayu.commands.CommandMessage.MESSAGE_MATCHING_CONTENTS;
import static kayu.commands.CommandMessage.MESSAGE_NO_MATCHING_CONTENTS;
import static kayu.commands.CommandType.FIND;

import java.util.Map;

import kayu.exception.DukeException;
import kayu.service.TaskList;
import kayu.task.Task;

/**
 * Represents a {@link kayu.commands.Command} that finds the relevant {@link kayu.task.Task}s
 * whose description matches with the keyword specified.
 */
public class FindCommand extends Command {

    /** Key word for command. */
    public static final String COMMAND_WORD = "find";

    /**
     * Initializes a Find- {@link kayu.commands.Command}.
     *
     * @param commandParams String parameters fed into the command by user.
     */
    public FindCommand(String commandParams) {
        super(FIND, commandParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute(TaskList taskList) throws DukeException {
        String keyword = commandParams.trim();
        if (keyword.isEmpty()) {
            throw new DukeException(String.format(ERROR_EMPTY_PARAMS, COMMAND_WORD));
        }

        Map<Integer, Task> taskMap = taskList.findTasksByDescriptionKeyword(keyword);
        if (taskMap.isEmpty()) {
            return String.format(MESSAGE_NO_MATCHING_CONTENTS, keyword);
        }
        
        String header = String.format(MESSAGE_MATCHING_CONTENTS, keyword);
        StringBuilder resultTasksAsString = new StringBuilder(header);
        taskMap.forEach((number, task) -> {
            String line = String.format("\n%d. %s", number + 1, task);
            resultTasksAsString.append(line);
        });
        
        return resultTasksAsString.toString();
    }
}
