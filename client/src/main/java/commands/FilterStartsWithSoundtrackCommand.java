package commands;

import collection.HumanObservableManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.HumanBeing;
import common.exceptions.MissedCommandArgumentException;
import controllers.MainWindowController;
import javafx.application.Platform;

import java.util.List;

public class FilterStartsWithSoundtrackCommand extends CommandImpl {
    private final HumanObservableManager collectionManager;

    public FilterStartsWithSoundtrackCommand(HumanObservableManager cm) {
        super("filter_starts_with_soundtrack", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        String start = getStringArg();
        List<HumanBeing> list = collectionManager.filterStartsWithSoundtrack(getStringArg());
        MainWindowController controller = collectionManager.getController();
        Platform.runLater(() -> controller.getFilter().filter(controller.getSoundtrackColumn(),
                "^" + getStringArg() + ".*",
                HumanBeing::getSoundtrackName));
        if (list.isEmpty()) return "Ни один из элементов не имеет песни, начинающейся с " + start;
        return list.stream()
                .sorted(new HumanBeing.SortingComparator())
                .map(HumanBeing::toString).reduce("", (a, b) -> a + b + "\n");
    }
}
