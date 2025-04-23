package net.whgkswo.tesm.conversation;

import net.minecraft.text.Text;

public record Decision(String text, Condition condition, String flowId) {
    public enum Condition{
        ALWAYS
        ;
    }

    public Text getText(){
        return Text.literal(text);
    }
}
