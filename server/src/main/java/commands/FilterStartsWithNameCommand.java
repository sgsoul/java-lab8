package commands;

import common.collection.HumanManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.HumanBeing;
import common.exceptions.MissedCommandArgumentException;

import java.util.List;

public class FilterStartsWithNameCommand extends CommandImpl {
    private final HumanManager collectionManager;

    public FilterStartsWithNameCommand(HumanManager cm) {
        super("filter_starts_with_name", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (hasStringArg()) throw new MissedCommandArgumentException();
        String start = getStringArg();
        List<HumanBeing> list = collectionManager.filterStartsWithName(getStringArg());
        if (list.isEmpty()) return "Ни один из элементов не имеет имени, начинающегося с " + start;
        return list.stream()
                .sorted(new HumanBeing.SortingComparator())
                .map(HumanBeing::toString).reduce("", (a, b) -> a + b + "\n");
    }
}

