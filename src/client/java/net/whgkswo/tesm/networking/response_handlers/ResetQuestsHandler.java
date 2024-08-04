package net.whgkswo.tesm.networking.response_handlers;

import net.minecraft.network.PacketByteBuf;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;

public class ResetQuestsHandler {
    public static void handle(){
        Quest.QUESTS.forEach(
                (key, value) -> {
                    value.setStatus(QuestStatus.AVAILABLE);
                    value.setCurrentStage(value.getStartingStage());
                }
        );
    }
}
