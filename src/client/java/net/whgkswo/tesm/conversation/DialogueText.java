package net.whgkswo.tesm.conversation;

import net.minecraft.text.Text;

import java.util.Queue;

public record DialogueText(String text, Queue<Action> actions) {
    public Text getText(){
        return Text.literal(text);
    }
}
