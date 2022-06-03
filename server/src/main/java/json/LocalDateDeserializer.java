package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import common.exceptions.InvalidDateFormatException;

import java.lang.reflect.Type;
import java.time.LocalDate;

import static common.utils.DateConverter.parseLocalDate;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return parseLocalDate(json.getAsJsonPrimitive().getAsString());
        } catch (InvalidDateFormatException e) {
            throw new JsonParseException("");
        }
    }
}