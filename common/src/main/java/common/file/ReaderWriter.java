package common.file;

import common.exceptions.FileException;

public interface ReaderWriter {
    String read() throws FileException;
}
