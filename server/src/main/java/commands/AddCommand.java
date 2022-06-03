package commands;

import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.CommandException;

import java.util.List;


public class AddCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public AddCommand(HumanManager cm) {
        super("add", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public Response run() throws CommandException {
        collectionManager.add(getHumanArg());
        return new AnswerMsg().info("Добавлен элемент: " + getHumanArg().toString()).setCollection(List.of(getHumanArg()));
    }
}
