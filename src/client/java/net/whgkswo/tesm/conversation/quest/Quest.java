package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.*;

public class Quest {
    private String name;
    private String description;
    private QuestType questType;
    private String subType;
    private List<QuestObjective> objectives;
    private boolean canBeStarted;
    public static final Map<String, Quest> availableQuest = new HashMap<>();
    public static final Map<String, Quest> onGoingQuest = new HashMap<>();
    public static final Map<String, Quest> completedQuest = new HashMap<>();
    public Quest(String name, String description, QuestType questType, String subType, QuestObjective... objectives){
        this.name = name;
        this.description = description;
        this.questType = questType;
        this.subType = subType;
        this.objectives = new ArrayList<>(Arrays.asList(objectives));
        availableQuest.put(name, this);
    }
    public static void registerQuests(){
        new Quest("테스트 퀘스트","테스트 퀘스트입니다.",QuestType.MISCELLANEOUS,"테스트",
                new QuestObjective("아탈리온과 대화하기", QuestObjective.ObjectiveType.TALK_TO, "아탈리온"));
    }
}
