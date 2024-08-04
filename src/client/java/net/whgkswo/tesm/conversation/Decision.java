package net.whgkswo.tesm.conversation;

import net.whgkswo.tesm.conversation.quest.Quest;

public class Decision extends DialogueLine{
    private boolean isChosen;
    private QuestRequirement questRequirement;
    private String targetQuestRoute;

    public Decision(String line) {
        super(line);
    }

    public Decision(String line, QuestRequirement questRequirement) {
        super(line);
        this.questRequirement = questRequirement;
        targetQuestRoute = Quest.QUEST_ROUTE_BASE_KEY;
    }
    public Decision(String line, QuestRequirement questRequirement, String targetQuestRoute) {
        super(line);
        this.questRequirement = questRequirement;
        this.targetQuestRoute = targetQuestRoute;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getTargetQuestRoute() {
        return targetQuestRoute;
    }

    public QuestRequirement getQuestRequirement() {
        return questRequirement;
    }
}
