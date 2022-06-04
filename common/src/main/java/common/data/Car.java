package common.data;

import java.io.Serializable;

public class Car implements Serializable {
    private final String name; //Поле может быть null

    public Car(String name) {
        this.name = name;
    }

    /**
     * @return Название машины.
     */
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }

    public boolean validate() {
        return (
                (name == null || (!name.equals("") && !(name.length() > 1237)))
        );
    }

}