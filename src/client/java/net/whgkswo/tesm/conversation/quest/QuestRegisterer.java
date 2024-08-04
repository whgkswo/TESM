package net.whgkswo.tesm.conversation.quest;

import net.whgkswo.tesm.conversation.quest.objective.QuestObjective;

import java.util.HashMap;

public class QuestRegisterer {
    public static void register(){
        new Quest("테스트 퀘스트","테스트 퀘스트입니다.",QuestType.MISCELLANEOUS,"테스트", "만남",
                new HashMap<>(){{
                    put("만남", new QuestStage("아탈리온과 대화하기", "CLEAR"));
                }});
        new Quest("테스트 퀘스트 2", "테스트 퀘스트 2입니다.", QuestType.MISCELLANEOUS, "테스트", "분기점 1",
                new HashMap<>(){{
                    put("분기점 1", new QuestStage(new HashMap<>(){{
                        put("아탈리온 루트", new QuestObjective("아탈리온과 대화하기", "결말"));
                        put("옥토 카마로 루트", new QuestObjective("옥토 카마로와 대화하기", "결말"));
                    }}));
                    put("결말", new QuestStage("인두리온에게 돌아가기", "CLEAR"));
                }});
    }
}
