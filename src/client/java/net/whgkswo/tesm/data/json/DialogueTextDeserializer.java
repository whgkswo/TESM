package net.whgkswo.tesm.data.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.whgkswo.tesm.conversation.Action;
import net.whgkswo.tesm.conversation.DialogueText;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DialogueTextDeserializer implements JsonDeserializer<DialogueText> {
    @Override
    public DialogueText deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        String textValue = json.get("text").getAsString();
        JsonArray actionValue = new JsonArray();
        if(json.has("actions")){
            actionValue = json.get("actions").getAsJsonArray();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Action.class, new ActionDeserializer());
        Gson gson = gsonBuilder.create();

        // TypeToken을 사용하여 List<DialogueText> 타입으로 변환
        Type listType = new TypeToken<List<Action>>(){}.getType();
        List<Action> actionsList = gson.fromJson(actionValue, listType);

        // List를 Queue로 변환
        Queue<Action> actions = new LinkedList<>(actionsList);
        return new DialogueText(textValue, actions);
    }
}
