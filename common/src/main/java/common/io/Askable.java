package common.io;

import common.exceptions.InvalidDataException;

@FunctionalInterface
public interface Askable<T> {
    T ask() throws InvalidDataException;
}