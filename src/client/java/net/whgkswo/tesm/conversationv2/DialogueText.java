package net.whgkswo.tesm.conversationv2;

import java.util.Queue;

public record DialogueText(String text, Queue<Action> actions) {
}
