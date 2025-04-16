package net.whgkswo.tesm.data.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.whgkswo.tesm.conversationv2.Action;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.DialogueText;
import net.whgkswo.tesm.conversationv2.Flow;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FlowDeserializer implements JsonDeserializer<Flow> {
    @Override
    public Flow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObject = json.getAsJsonObject();

        String typeValue = jsonObject.get("type").getAsString();
        String textsValue = jsonObject.get("texts").getAsString();
        JsonArray afterActionsValue = new JsonArray();
        if(jsonObject.has("after")) afterActionsValue = jsonObject.getAsJsonArray("after");

        Flow.Type type = Flow.Type.valueOf(typeValue);

        Queue<DialogueText> texts = ConversationHelper.getTexts(textsValue);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Action.class, new ActionDeserializer());
        Gson gson = gsonBuilder.create();

        // TypeToken을 사용하여 List<AfterAction> 타입으로 변환
        Type listType = new TypeToken<List<Action>>(){}.getType();
        List<Action> dialogueTextList = gson.fromJson(afterActionsValue, listType);

        // List를 Queue로 변환
        Queue<Action> actions = new LinkedList<>(dialogueTextList);

        return new Flow(type, texts, actions);
    }
}
