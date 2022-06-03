package common.commands;

import common.connection.Request;
import common.connection.Response;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;

public interface Commandable {

    void addCommand(Command cmd);

    Response runCommand(Request req);

    Command getCommand(String key);

    default Command getCommand(Request req) {
        return getCommand(req.getCommandName());
    }

    boolean hasCommand(String s);

    default boolean hasCommand(Request req) {
        return hasCommand(req.getCommandName());
    }

    void consoleMode();

    Response fileMode(String path) throws FileException, InvalidDataException, ConnectionException;
}
