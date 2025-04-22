package net.whgkswo.tesm.conversationv2;

import net.minecraft.text.Text;

public record DecisionV2(String text, Condition condition, String flowId) {
    public enum Condition{
        ALWAYS
        ;
    }

    public Text getText(){
        return Text.literal(text);
    }
}
