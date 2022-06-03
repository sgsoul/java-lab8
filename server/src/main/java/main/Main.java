package main;

import common.exceptions.ConnectionException;
import common.exceptions.DatabaseException;
import common.exceptions.InvalidPortException;
import log.Log;
import server.Server;

import java.util.Properties;

/**
 * основной класс для запуска сервера с аргументами
 */

public class Main {
    public static void main(String[] args) {
        int port;
        String strPort = "5432";
        String user = "postgres";
        String password = "arina";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        args = new String[]{"5432", "postgres", "postgres", "arina"};
        try {
            if (args.length == 4) {
                strPort = args[0];
                user = args[2];
                password = args[3];

            }
            if (args.length == 1) strPort = args[0];
            if (args.length == 0) Log.logger.info("нет порта, переданного аргументом, размещенного на " + strPort);
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new InvalidPortException();
            }
            Properties settings = new Properties();
            settings.setProperty("url", url);
            settings.setProperty("user", user);
            settings.setProperty("password", password);
            Server server = new Server(port, settings);

            server.start();
            server.consoleMode();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (server.isRunning()) server.close();
            }, "поток завершения работы"));

        } catch (ConnectionException | DatabaseException e) {
            Log.logger.error(e.getMessage());
        }
    }
}
