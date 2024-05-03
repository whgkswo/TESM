package net.whgkswo.tesm.npcs.quests;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.overlay.QuestStartAndClear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class Quests {
    //public static ArrayList<String[]> availableQues = new ArrayList<String[]>();

    public static HashMap<String,String[]> availableQuests = new LinkedHashMap<>();
    public static ArrayList<String[]> currentQuests = new ArrayList<String[]>();
    public static HashMap<String,String[]> currneQuestsMap = new LinkedHashMap<>();
    public static ArrayList<String[]> completedQuests = new ArrayList<String[]>();
    public static HashMap<String,String[]> completedQuestsMap = new LinkedHashMap<>();
    public static int questNameOLState = 0;
    public static boolean questObjectiveOLOn = false;
    public static final SoundEvent NEW_QUEST_SOUND = SoundEvent.of(new Identifier(TESMMod.MODID,"new_quest"));
    public static final SoundEvent QUEST_OBJECTIVE_SOUND = SoundEvent.of(new Identifier(TESMMod.MODID,"new_objective_2"));
    public static final SoundEvent QUEST_CLEAR_SOUND = SoundEvent.of(new Identifier(TESMMod.MODID,"quest_completed"));
    static PlayerEntity player = MinecraftClient.getInstance().player;
    static World world = MinecraftClient.getInstance().world;
    public static String updatingQuestName;
    public static String updatingObjective;

    static { // 0: 퀘스트 이름 , 1:설명,  2:진행 단계+목표,  3:보상
        availableQuests.put("테스트 퀘스트", new String[]{
                "테스트 퀘스트입니다.\n두 번째 줄입니다.",
                "3\n퀘스트 목표\n퀘스트 목표 2\n퀘스트 목표 3",
                ""});
        availableQuests.put("테스트 퀘스트 2", new String[]{
                "테스트 퀘스트 2입니다.\n두 번째 줄입니다.",
                "3\n퀘스트 목표\n퀘스트 목표 2\n퀘스트 목표 3",
                ""});

        /*for(int i=0;i<6;i++){
            currentQuests.add( new String[]{
                    "테스트 퀘스트",
                    "테스트 퀘스트입니다.\n두 번째 줄입니다.",
                    "3\n퀘스트 목표\n퀘스트 목표 2\n퀘스트 목표 3",
                    ""});
            currentQuests.add( new String[]{
                    "테스트 퀘스트 2",
                    "테스트 퀘스트 2입니다.",
                    "0\n퀘스트 목표\n퀘스트 목표 2\n퀘스트 목표 3",
                    ""});
            currentQuests.add( new String[]{
                    "이름이 엄청 긴 테스트 퀘스트 3",
                    "테스트 퀘스트 3입니다.",
                    "0\n퀘스트 목표\n퀘스트 목표 2\n퀘스트 목표 3",
                    ""});
        }*/
    }
    public static void startQuest(String questName){
        ArrayList<String> tempList = new ArrayList<>(List.of(availableQuests.get(questName)));
        updatingQuestName = questName;
        updatingObjective = tempList.get(1).split("\n")[1];
        tempList.add(0, questName);

        // 진행 중인 퀘스트는 링크드 해쉬맵과 어레이리스트가 동시에 존재함 (특정 키 값으로 접근용, n번째 ~ n+k번째 요소 나열용)
        currentQuests.add(new String[]{tempList.get(0),tempList.get(1),tempList.get(2),tempList.get(3)});
        currneQuestsMap.put(questName, availableQuests.get(questName));

        // 오버레이 시각 효과
        world.playSound(player,player.getBlockPos(),NEW_QUEST_SOUND, SoundCategory.RECORDS);
        QuestStartAndClear.alpha = 24;
        questNameOLState = 1;
        new Thread(()->{
            try{
                // 페이드 인
                for(int i = 0;i<9;i++){
                    Thread.sleep(40);
                    QuestStartAndClear.alpha = QuestStartAndClear.alpha + 24;
                }
                Thread.sleep(40);
                QuestStartAndClear.alpha = 255;
                Thread.sleep(3000);
                //페이드 아웃
                for(int i=0;i<20;i++){
                    Thread.sleep(50);
                    QuestStartAndClear.alpha = QuestStartAndClear.alpha - 12;
                }
                questNameOLState = 0;
                world.playSound(player,player.getBlockPos(),QUEST_OBJECTIVE_SOUND,SoundCategory.RECORDS);
                questObjectiveOLOn = true;
                Thread.sleep(3000);
                questObjectiveOLOn = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void clearQuest(String questName){
        updatingQuestName = questName;
        // 진행 중 + 완료된 퀘스트 목록 업데이트
        for(int i=0;i<currentQuests.size();i++){
            if(questName.equals(currentQuests.get(i)[0])){
                // 어레이리스트 업데이트
                completedQuests.add(currentQuests.get(i));
                currentQuests.remove(i);
                break;
            }
        } // 링크드해쉬맵 업데이트
        completedQuestsMap.put(questName,currneQuestsMap.get(questName));
        currneQuestsMap.remove(questName);

        // 오버레이 시각 효과
        world.playSound(player,player.getBlockPos(),QUEST_CLEAR_SOUND, SoundCategory.RECORDS);
        QuestStartAndClear.alpha = 24;
        questNameOLState = 2;
        new Thread(()->{
            try{
                // 페이드 인
                for(int i = 0;i<9;i++){
                    Thread.sleep(40);
                    QuestStartAndClear.alpha = QuestStartAndClear.alpha + 24;
                }
                Thread.sleep(40);
                QuestStartAndClear.alpha = 255;
                Thread.sleep(3000);
                //페이드 아웃
                for(int i=0;i<20;i++){
                    Thread.sleep(50);
                    QuestStartAndClear.alpha = QuestStartAndClear.alpha - 12;
                }
                questNameOLState = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
