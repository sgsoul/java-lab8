package commands;

import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class InfoCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public InfoCommand(HumanManager cm) {
        super("info", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        return collectionManager.getInfo();
    }

}
