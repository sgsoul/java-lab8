package common.io;

import common.exceptions.FileException;
import common.file.FileManager;

import java.util.Scanner;

/**
 * Операции с выводом.
 */

public class FileInputManager extends InputManagerImpl {
    public FileInputManager(String path) throws FileException {
        super(new Scanner(new FileManager(path).read()));
        getScanner().useDelimiter("\n");
    }
}
