package net.whgkswo.tesm.conversation.v2;

import net.whgkswo.tesm.conversation.v2.Decision;
import net.whgkswo.tesm.conversation.v2.DialogueStage;
import net.whgkswo.tesm.conversation.v2.DialogueType;

import java.util.List;

public class DecisionStage extends DialogueStage {
    private List<Decision> contents;
    public DecisionStage(DialogueType dialogueType, List<Decision> contents) {
        super(dialogueType);
        this.contents = contents;
    }
    public List<Decision> getContents() {
        return contents;
    }
}
