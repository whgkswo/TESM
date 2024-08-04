package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.HashMap;
import java.util.Map;

public class QuestStage {
    private Map<String, QuestObjective> objectives;

    public QuestStage(Map<String, QuestObjective> objectives) {
        this.objectives = objectives;
    }
    public QuestStage(String description, String nextStage){
        this.objectives = new HashMap<>(){{
            put(Quest.QUEST_ROUTE_BASE_KEY, new QuestObjective(description, nextStage));
        }};
    }
    public QuestStage(Map.Entry<String, QuestObjective>... entries) {
        objectives = new HashMap<>(){{
            for(Map.Entry<String, QuestObjective> entry : entries){
                put(entry.getKey(), entry.getValue());
            }
        }};
    }

    public Map<String, QuestObjective> getObjectives() {
        return objectives;
    }
}
