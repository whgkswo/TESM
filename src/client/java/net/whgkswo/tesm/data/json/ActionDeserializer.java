package net.whgkswo.tesm.data.json;

import com.google.gson.*;
import net.whgkswo.tesm.conversationv2.Action;

import java.lang.reflect.Type;

public class ActionDeserializer implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        String typeStr = json.get("type").getAsString();
        Action.Type actionType = Action.Type.valueOf(typeStr);

        String target = json.get("target").getAsString();

        return new Action(actionType, target);
    }
}
