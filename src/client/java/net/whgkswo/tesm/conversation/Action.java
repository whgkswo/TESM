package net.whgkswo.tesm.conversation;

public record Action(Type type, String target) {
    public enum Type{
        NAME_REVEAL,
        SHOW_DECISIONS
        ;
    }
}
