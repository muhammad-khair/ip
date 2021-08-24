package commands;

import exception.DukeException;
import service.TaskList;

public class InvalidCommand extends Command {

    public InvalidCommand() {
        super(CommandType.INVALID);
    }

    @Override
    public String execute(TaskList taskList) throws DukeException {
        throw new DukeException("");
    }
}
