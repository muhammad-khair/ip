package command;

import exception.DukeException;
import service.TaskList;

public abstract class Command {
    
    private final CommandType commandType;
    
    protected final String commandParams;

    public Command(CommandType commandType, String commandParams) {
        this.commandType = commandType;
        this.commandParams = commandParams;
    }

    public Command(CommandType commandType) {
        this.commandType = commandType;
        this.commandParams = "";
    }

    public abstract String execute(TaskList taskList) throws DukeException;
    
    public CommandType getCommandType() {
        return commandType;
    }
    
    public boolean isBye() {
        return commandType.equals(CommandType.BYE);
    }
}
