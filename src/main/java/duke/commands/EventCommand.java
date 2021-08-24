package duke.commands;

import duke.exception.DukeException;
import duke.service.TaskList;
import duke.task.Event;
import duke.task.Task;
import duke.parser.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventCommand extends AddTaskCommand {

    public static final String COMMAND_WORD = "event";

    public EventCommand(String commandParams, DateTimeFormat dateTimeFormat) {
        super(CommandType.EVENT, commandParams, dateTimeFormat);
    }

    @Override
    public String execute(TaskList taskList) throws DukeException {
        String[] paramArray = super.splitUserParams(commandParams, COMMAND_WORD, Event.SPLIT_WORD);
        String desc = super.extractDesc(paramArray, COMMAND_WORD);
        LocalDate atDate = super.extractDate(paramArray);
        LocalTime atTime = super.extractTime(paramArray);
        
        Task event = new Event(desc, atDate, atTime);
        taskList.addTask(event);
        return String.format(CommandMessage.MESSAGE_CREATED_EVENT, event, taskList.getCapacity());
    }
}
