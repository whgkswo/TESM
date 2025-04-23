package net.whgkswo.tesm.conversation;

import java.util.Queue;

public record Flow(Type flowType, Queue<DialogueText> texts, Queue<Action> actions) {
    public enum Type{
        SHOW_TEXTS,
        ;
    }
}
