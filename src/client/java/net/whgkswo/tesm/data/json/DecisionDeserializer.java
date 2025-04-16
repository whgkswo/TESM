package net.whgkswo.tesm.data.json;

import com.google.gson.*;
import net.whgkswo.tesm.conversationv2.DecisionV2;

import java.lang.reflect.Type;

public class DecisionDeserializer implements JsonDeserializer<DecisionV2> {
    @Override
    public DecisionV2 deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        String textValue = json.get("text").getAsString();
        String conditionValue = json.has("condition") ? json.get("condition").getAsString() : "ALWAYS";
        String flowValue = json.has("flow") ? json.get("flow").getAsString() : "";

        DecisionV2.Condition condition = DecisionV2.Condition.valueOf(conditionValue);

        return new DecisionV2(textValue, condition, flowValue);
    }
}
