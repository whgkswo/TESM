package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.*;

public class Quest {
    private QuestStatus status;
    private String name;
    private String description;
    private QuestType questType;
    private String subType;
    private List<QuestObjective> objectives;
    public static final Map<String, Quest> QUESTS = new HashMap<>();
    public Quest(String name, String description, QuestType questType, String subType, QuestObjective... objectives){
        status = QuestStatus.AVAILABLE;
        this.name = name;
        this.description = description;
        this.questType = questType;
        this.subType = subType;
        this.objectives = new ArrayList<>(Arrays.asList(objectives));
        QUESTS.put(name, this);
    }
    public static void registerQuests(){
        new Quest("테스트 퀘스트","테스트 퀘스트입니다.",QuestType.MISCELLANEOUS,"테스트",
                new QuestObjective("아탈리온과 대화하기"));
    }

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public String getSubType() {
        return subType;
    }

    public List<QuestObjective> getObjectives() {
        return objectives;
    }
}
