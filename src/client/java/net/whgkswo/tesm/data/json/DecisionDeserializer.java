package net.whgkswo.tesm.data.json;

import com.google.gson.*;
import net.whgkswo.tesm.conversation.Decision;

import java.lang.reflect.Type;

public class DecisionDeserializer implements JsonDeserializer<Decision> {
    @Override
    public Decision deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        String textValue = json.get("text").getAsString();
        String conditionValue = json.has("condition") ? json.get("condition").getAsString() : "ALWAYS";
        String flowValue = json.has("flow") ? json.get("flow").getAsString() : "";

        Decision.Condition condition = Decision.Condition.valueOf(conditionValue);

        return new Decision(textValue, condition, flowValue);
    }
}
