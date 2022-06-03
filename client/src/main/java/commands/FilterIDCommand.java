package commands;

import collection.HumanObservableManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.HumanBeing;
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
    public String execute() {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        String start = getStringArg();
        List<HumanBeing> list = collectionManager.filterID(getHumanArg().getId());
        MainWindowController controller = collectionManager.getController();
   /*     Platform.runLater(() -> {
            controller.getFilter().filter(controller.getIDColumn(),
                    "^" + getStringArg() + ".*",
                    HumanBeing::getId);
        }); */
        if (list.isEmpty()) return "Ни один из элементов не имеет id " + start;
        return list.stream()
                .sorted(new HumanBeing.SortingComparator())
                .map(HumanBeing::toString).reduce("", (a, b) -> a + b + "\n");
    }
}
