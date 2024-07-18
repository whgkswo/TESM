package net.whgkswo.tesm.conversation.v2;

import net.whgkswo.tesm.conversation.v2.DialogueStage;
import net.whgkswo.tesm.conversation.v2.DialogueType;

import java.util.List;

public class NormalStage extends DialogueStage {
    private List<String> contents;
    private ExecuteAfter executeAfter;
    public NormalStage(DialogueType dialogueType, List<String> contents, ExecuteAfter executeAfter) {
        super(dialogueType);
        this.contents = contents;
        this.executeAfter = executeAfter;
    }

    public List<String> getContents() {
        return contents;
    }

    public ExecuteAfter getExecuteAfter() {
        return executeAfter;
    }

    public enum ExecuteAfter{
        SHOW_DECISIONS,
        JUMP_TO,
        START_QUEST,
        EXIT
    }
}
