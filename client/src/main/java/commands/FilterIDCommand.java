package commands;

import collection.HumanObservableManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.HumanBeing;
import common.exceptions.InvalidDataException;
import common.exceptions.MissedCommandArgumentException;
import controllers.MainWindowController;
import javafx.application.Platform;

import java.util.List;

public class FilterIDCommand extends CommandImpl {
    private final HumanObservableManager collectionManager;

    public FilterIDCommand(HumanObservableManager cm) {
        super("filter_id", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        try {
            String start = getStringArg();
            List<HumanBeing> list = collectionManager.filterID(Integer.parseInt(getStringArg()));
            MainWindowController controller = collectionManager.getController();
            Platform.runLater(() -> {
                controller.getFilter().filter(controller.getIDColumn(),
                        "^" + getStringArg() + "$",
                        human -> String.valueOf(human.getId()));
            });
            if (list.isEmpty()) return "Ни один из элементов не имеет id " + start;
            return list.stream()
                    .sorted(new HumanBeing.SortingComparator())
                    .map(HumanBeing::toString).reduce("", (a, b) -> a + b + "\n");
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[IdFormatException]");
        }
    }
}
