package commands;

import exception.DukeException;
import service.TaskList;

public class ByeCommand extends Command {

    public static final String COMMAND_WORD = "bye";

    public ByeCommand() {
        super(CommandType.BYE);
    }

    @Override
    public String execute(TaskList taskList) throws DukeException {
        return ""; // fall through
    }
}
