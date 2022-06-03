package common.commands;

import common.connection.AnswerMsg;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.exceptions.*;
import common.io.ConsoleInputManager;
import common.io.FileInputManager;
import common.io.InputManager;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import static common.io.ConsoleOutputter.print;


public abstract class CommandManager implements Commandable, Closeable {
    private final Map<String, Command> map;
    protected InputManager inputManager;
    protected boolean isRunning;
    protected String currentScriptFileName;
    private static final Stack<String> callStack = new Stack<>();

    public Stack<String> getStack() {
        return callStack;
    }

    public CommandManager() {
        isRunning = false;
        currentScriptFileName = "";
        map = new HashMap<>();
    }

    public void addCommand(Command c) {
        map.put(c.getName(), c);
    }

    public Command getCommand(String s) {
        if (!hasCommand(s)) throw new NoSuchCommandException(s);
        return map.get(s);
    }

    public boolean hasCommand(String s) {
        return map.containsKey(s);
    }

    public void consoleMode() {
        inputManager = new ConsoleInputManager();
        isRunning = true;

        while (isRunning) {
            Response answerMsg = new AnswerMsg();
            print("Врубай клиента");
            try {
                CommandMsg commandMsg = inputManager.readCommand();
                answerMsg = runCommand(commandMsg);
            } catch (InvalidDataException ignored){}
            catch (NoSuchElementException e) {
                close();
                print("Пользовательский ввод недоступен.");
                break;
            }
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }

    public Response fileMode(String path) throws FileException, InvalidDataException, ConnectionException {
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        Response answerMsg = new AnswerMsg();
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
                break;
            }
            if(answerMsg.getStatus()== Response.Status.ERROR){
                break;
            }
        }
        return answerMsg;
    }

    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            res = (AnswerMsg) runCommandUnsafe(msg);
        } catch (ExitException e) {
            res.setStatus(Response.Status.EXIT);
        } catch (CommandException | InvalidDataException | ConnectionException | FileException | CollectionException e) {
            res.error(e.getMessage());
        }
        return res;
    }

    public Response runCommandUnsafe(Request msg) throws CommandException, InvalidDataException, ConnectionException, FileException, CollectionException {
        AnswerMsg res;
        Command cmd = getCommand(msg);
        cmd.setArgument(msg);
        res = (AnswerMsg) cmd.run();
        res.setCollectionOperation(cmd.getOperation());

        return res;
    }

    public static String getHelp() {
        return """
                \s
                register {user} : register a new user

                login {user} : login user

                help : показать справку для доступных команд

                info : запись в стандартный вывод информации о коллекции (тип,
                дата инициализации, количество элементов и т.д.)

                show : вывести на стандартный вывод все элементы коллекции в
                строковое представление

                add {element} : добавление новый элемент в коллекцию

                update id {element} : обновите значение элемента коллекции по идентификатору

                remove_by_id id : удалить элемент из коллекции по его идентификатору

                clear : очистить коллекцию

                save (file_name - optional) : сохраните коллекцию в общий файл

                load (file_name - optional): загрузить коллекцию из common.file

                execute_script file_name : считайте и выполняйте скрипт из указанного общего файла.
                Скрипт содержит команды в той же форме, в которой они вводятся
                пользователь является интерактивным.

                exit : выйдите из программы (без сохранения в общий файл)

                remove_first : удалите первый элемент из коллекции

                add_if_max {element} : добавьте новый элемент в коллекцию, если его

                значение больше значения самого большого элемента этой коллекции

                add_if_min {element} : добавьте новый элемент в коллекцию, если он
                значение меньше, чем наименьший элемент этой коллекции

                print_average_of_minutes_of_waiting : выведите среднее значение
                по полю время ожидания

                filter_starts_with_name name : выходные элементы, значение имени поля
                который начинается с заданной подстроки

                print_unique_impact_speed : выведите уникальные значения поля скорости удара""";

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void close() {
        setRunning(false);
    }
}
