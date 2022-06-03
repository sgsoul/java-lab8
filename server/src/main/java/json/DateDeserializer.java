package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import common.exceptions.InvalidDateFormatException;

import java.lang.reflect.Type;
import java.util.Date;

import static common.utils.DateConverter.parseDate;

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return parseDate(json.getAsJsonPrimitive().getAsString());
        } catch (InvalidDateFormatException e) {
            throw new JsonParseException("");
        }
    }
}