package net.whgkswo.tesm.conversation.v2;

public class DialogueStage {
    private DialogueType dialogueType;

    public DialogueType getDialogueType() {
        return dialogueType;
    }

    public DialogueStage(DialogueType dialogueType) {
        this.dialogueType = dialogueType;
    }
}
