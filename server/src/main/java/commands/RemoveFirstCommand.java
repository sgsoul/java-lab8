package commands;

import common.auth.User;
import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.HumanBeing;
import common.exceptions.AuthException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.PermissionException;

import java.util.List;


public class RemoveFirstCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public RemoveFirstCommand(HumanManager cm) {
        super("remove_first", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;
    }


    @Override
    public Response run() throws AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        HumanBeing human = collectionManager.getCollection().iterator().next();
        int id = human.getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String humanCreatorLogin = user.getLogin();
        if (humanCreatorLogin == null || !humanCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        collectionManager.removeFirst();
        return new AnswerMsg().info("Элемент #" + id + " удалён.").setCollection(List.of(human));
    }
}