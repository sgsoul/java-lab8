package commands;

import common.auth.User;
import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.exceptions.*;

import java.util.List;

import static common.utils.Parser.parseId;

public class UpdateCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public UpdateCommand(HumanManager cm) {
        super("update", CommandType.NORMAL, CollectionOperation.UPDATE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg() || !hasHumanArg())  throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("ID не найден.");
        String owner = collectionManager.getByID(id).getUserLogin();
        String humanCreatorLogin = user.getLogin();
        if (humanCreatorLogin == null || !humanCreatorLogin.equals(owner))
            throw new PermissionException(owner);

        collectionManager.updateByID(id, getHumanArg());
        return new AnswerMsg().info( "Элемент #" + id + " обновлён.").setCollection(List.of(getHumanArg())).setCollectionOperation(CollectionOperation.UPDATE);
    }

}
