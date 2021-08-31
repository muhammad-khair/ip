package kayu.commands;

import static kayu.commands.CommandMessage.MESSAGE_CREATED_EVENT;
import static kayu.commands.CommandType.EVENT;

import java.time.LocalDate;
import java.time.LocalTime;

import kayu.exception.DukeException;
import kayu.parser.DateTimeFormat;
import kayu.service.TaskList;
import kayu.task.Event;
import kayu.task.Task;

/**
 * Represents an {@link kayu.commands.AddTaskCommand} that creates a {@link kayu.task.Event}
 * and saves it in {@link kayu.service.TaskList}.
 */
public class EventCommand extends AddTaskCommand {

    /** Key word for command. */
    public static final String COMMAND_WORD = "event";

    /**
     * Initializes an Event- {@link kayu.commands.AddTaskCommand}.
     *
     * @param commandParams String parameters fed into the command by user.
     * @param dateTimeFormat {@link kayu.parser.DateTimeFormat} used in parsing, if required.
     */
    public EventCommand(String commandParams, DateTimeFormat dateTimeFormat) {
        super(EVENT, commandParams, dateTimeFormat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute(TaskList taskList) throws DukeException {
        String[] paramArray = super.splitUserParams(commandParams, COMMAND_WORD, Event.SPLIT_WORD);
        
        String desc = super.extractDesc(paramArray, COMMAND_WORD);
        LocalDate atDate = super.extractDate(paramArray);
        LocalTime atTime = super.extractTime(paramArray);
        
        Task event = new Event(desc, atDate, atTime);
        taskList.addTask(event);
        return String.format(MESSAGE_CREATED_EVENT, event, taskList.getCapacity());
    }
}
