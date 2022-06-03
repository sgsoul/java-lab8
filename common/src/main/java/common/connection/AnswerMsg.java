package common.connection;

import common.data.HumanBeing;

import java.util.Collection;

/**
 * Message witch server send to client
 */
public class AnswerMsg implements Response {
    private static final long serialVersionUID = 666;
    private String msg;
    private Status status;
    private Collection<HumanBeing> collection;
    private CollectionOperation collectionOperation;

    public AnswerMsg() {
        msg = "";
        status = Status.FINE;
        collectionOperation = CollectionOperation.NONE;
    }

    public AnswerMsg clear() {
        msg = "";
        return this;
    }

    public AnswerMsg info(Object str) {
        msg = str.toString();// + "\n";
        return this;
    }

    public AnswerMsg error(Object str) {
        msg = /*"Error: " + */str.toString();// + "\n";
        setStatus(Status.ERROR);
        return this;
    }

    public AnswerMsg setStatus(Status st) {
        status = st;
        return this;
    }

    public AnswerMsg setCollectionOperation(CollectionOperation op) {
        collectionOperation = op;
        return this;
    }

    public CollectionOperation getCollectionOperation() {
        return collectionOperation;
    }


    public AnswerMsg setCollection(Collection<HumanBeing> c) {
        collection = c;
        return this;
    }

    public Collection<HumanBeing> getCollection() {
        return collection;
    }

    public String getMessage() {
        return msg;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public String toString() {
        if (getStatus() == Status.ERROR) {
            return "Err: " + getMessage();
        }
        return getMessage();
    }
}