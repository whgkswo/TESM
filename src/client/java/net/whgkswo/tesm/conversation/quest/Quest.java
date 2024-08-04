package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.Decision;
import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;
import net.whgkswo.tesm.gui.overlay.QuestOverlay;

import java.util.*;

public class Quest {
    public static final String QUEST_ROUTE_BASE_KEY = "기본 키";
    private String startingStage;
    private QuestStatus status;
    private String name;
    private String description;
    private QuestType questType;
    private String subType;
    // TODO: 선형적 퀘스트가 아니라 분기를 가진 퀘스트도 있어야 함. 이를 위해 objectiveStage 도입 필요
    private Map<String, QuestStage> objectives;
    private String currentStage;
    public static final Map<String, Quest> QUESTS = new HashMap<>();
    public Quest(String name, String description, QuestType questType, String subType, String startingStage, HashMap<String,QuestStage> stages){
        status = QuestStatus.AVAILABLE;
        this.name = name;
        this.description = description;
        this.questType = questType;
        this.subType = subType;
        this.startingStage = startingStage;
        this.objectives = stages;
        currentStage = startingStage;
        QUESTS.put(name, this);
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

    public String getStartingStage() {
        return startingStage;
    }

    public Map<String, QuestStage> getStages() {
        return objectives;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public static void startQuest(String questName){
        Quest quest = Quest.QUESTS.get(questName);
        quest.setStatus(QuestStatus.ONGOING);

        Map<String, QuestObjective> objectives = quest.getStages().get(quest.getStartingStage()).getObjectives();
        // 퀘스트 시작 팝업 띄우기
        QuestOverlay.displayStartPopUp("시작", questName, objectives);
    }
    public static void advanceQuest(String questName, Decision selectedDecision){
        Quest quest = Quest.QUESTS.get(questName);

        Map<String, QuestStage> stages = quest.getStages();
        Map<String, QuestObjective> objectives = stages.get(quest.getCurrentStage()).getObjectives();
        QuestObjective objective = objectives.get(selectedDecision.getTargetQuestRoute());

        String nextStage = objective.getNextStage();
        quest.setCurrentStage(nextStage);
        // 퀘스트 진행 상황 팝업 띄우기
        String prevObjective = objectives.get(selectedDecision.getTargetQuestRoute()).getDescription();
        Map<String, QuestObjective> nextObjectives = stages.get(nextStage).getObjectives();
        QuestOverlay.displayAdvancePopUp(prevObjective, nextObjectives);
    }
    public static void completeQuest(String questName){
        Quest quest = Quest.QUESTS.get(questName);
        quest.setStatus(QuestStatus.COMPLETED);
        // 퀘스트 완료 팝업 띄우기
        QuestOverlay.displayCompletePopUp("완료", questName);
    }
}
