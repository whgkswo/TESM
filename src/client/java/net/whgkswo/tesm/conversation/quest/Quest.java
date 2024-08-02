package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.ConversationScreen;
import net.whgkswo.tesm.conversation.Decision;
import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;
import net.whgkswo.tesm.gui.overlay.QuestOverlay;

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

    public static void startQuest(String questName){
        Quest quest = Quest.QUESTS.get(questName);
        quest.setStatus(QuestStatus.ONGOING);
        // 퀘스트 시작 팝업 띄우기
        QuestOverlay.displayPopUp("시작", questName);
    }
    public static void advanceQuest(String questName, Decision selectedDecision){
        Quest quest = Quest.QUESTS.get(questName);

        Map<String, QuestObjective> objectives = quest.getObjectives();
        String nextStage = objectives.get(selectedDecision.getQuestObjectId()).getNextStage();
        quest.setCurrentStage(nextStage);
    }
    public static void completeQuest(String questName){
        Quest quest = Quest.QUESTS.get(questName);
        quest.setStatus(QuestStatus.COMPLETED);
        // 퀘스트 완료 팝업 띄우기
        QuestOverlay.displayPopUp("완료", questName);
    }

    public static void registerQuests(){
        new Quest("테스트 퀘스트","테스트 퀘스트입니다.",QuestType.MISCELLANEOUS,"테스트",
                new HashMap<>(){{
                    put("1:1", new QuestObjective("아탈리온과 대화하기", "CLEAR"));
                }});
        new Quest("테스트 퀘스트 2", "테스트 퀘스트 2입니다.", QuestType.MISCELLANEOUS, "테스트",
                new HashMap<>(){{
                    put("1:1", new QuestObjective("아탈리온과 대화하기", "2"));
                    put("1:2", new QuestObjective("또는 옥토 카마로와 대화하기", "2"));
                    put("2:1", new QuestObjective("인두리온에게 돌아가기", "CLEAR"));
                }});
    }
}
