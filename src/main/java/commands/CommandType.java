package commands;

/**
 * Command enum class.
 *
 * This enum class is used by the Parser and helps direct the TaskList
 * to the right operations. They follow the user inputs used to trigger such commands.
 */
public enum CommandType {
    BYE,
    LIST,
    DONE,
    DELETE,
    TODO,
    EVENT,
    DEADLINE,
    INVALID;
}
