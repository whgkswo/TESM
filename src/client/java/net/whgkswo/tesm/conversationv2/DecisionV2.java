package net.whgkswo.tesm.conversationv2;

import java.util.function.Predicate;

public record DecisionV2(String text, Condition condition, String flowId) {
    public enum Condition{
        ALWAYS
        ;
    }
}
