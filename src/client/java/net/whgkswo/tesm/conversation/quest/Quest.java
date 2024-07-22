package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.*;

public class Quest {
    private QuestStatus status;
    private String name;
    private String description;
    private QuestType questType;
    private String subType;
    // TODO: 선형적 퀘스트가 아니라 분기를 가진 퀘스트도 있어야 함. 이를 위해 objectiveStage 도입 필요
    private Map<String,QuestObjective> objectives;
    private String currentStage = "1";
    public static final Map<String, Quest> QUESTS = new HashMap<>();
    public Quest(String name, String description, QuestType questType, String subType, HashMap<String,QuestObjective> objectives){
        status = QuestStatus.AVAILABLE;
        this.name = name;
        this.description = description;
        this.questType = questType;
        this.subType = subType;
        this.objectives = objectives;
        QUESTS.put(name, this);
    }
    public static void registerQuests(){
        new Quest("테스트 퀘스트","테스트 퀘스트입니다.",QuestType.MISCELLANEOUS,"테스트",
                new HashMap<>(){{
                    put("1", new QuestObjective("아탈리온과 대화하기"));
                }});
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

    public Map<String, QuestObjective> getObjectives() {
        return objectives;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }
}
