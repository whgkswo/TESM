package net.whgkswo.tesm.conversation;

public class Decision extends DialogueLine{
    private boolean isChosen;
    private QuestRequirement questRequirement;

    public Decision(String line) {
        super(line);
    }

    public Decision(String line, QuestRequirement questRequirement) {
        super(line);
        this.questRequirement = questRequirement;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public QuestRequirement getQuestRequirement() {
        return questRequirement;
    }
}
