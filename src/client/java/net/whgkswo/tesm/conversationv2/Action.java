package net.whgkswo.tesm.conversationv2;

public record Action(Type type, String target) {
    public enum Type{
        NAME_REVEAL(),
        SHOW_DECISIONS()
        ;
    }
}
