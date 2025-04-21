package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.gui.helpers.GuiHelper;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.npcs.quests.Quests;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static net.whgkswo.tesm.general.GlobalVariablesClient.arrowState;

public class JournalScreen extends TesmScreen {
    public JournalScreen() {
        super();
    }

    private final Identifier MENU_BACKGROUND = Identifier.of(TESMMod.MODID, "textures/gui/menu_background.png");
    private final Identifier TEXTURE_AAA685 = Identifier.of(TESMMod.MODID, "textures/gui/texture_aaa685.png");
    private final Identifier HOR_AAA685 = Identifier.of(TESMMod.MODID, "textures/gui/horizental_aaa685.png");
    private final Identifier VER_AAA685 = Identifier.of(TESMMod.MODID,"textures/gui/vertical_aaa685.png");
    private final Identifier Q_KEY_ICON = Identifier.of(TESMMod.MODID,"textures/gui/q_key.png");
    private final Identifier E_KEY_ICON = Identifier.of(TESMMod.MODID,"textures/gui/e_key.png");
    private final Identifier QUESTBAR_SIDE = Identifier.of(TESMMod.MODID, "textures/gui/questbar_side.png");
    private final Identifier VER_LINE_AA9682 = Identifier.of(TESMMod.MODID,"textures/gui/vertical_line_aa9682.png");
    private final Identifier QUESTLIST_TAB = Identifier.of(TESMMod.MODID,"textures/gui/questlist_tab.png");
    private final Identifier SELECTED_QUEST = Identifier.of(TESMMod.MODID,"textures/gui/selected_quest.png");
    private final Identifier UP_ARROW = Identifier.of(TESMMod.MODID,"textures/gui/uparrow.png");
    private final Identifier DOWN_ARROW = Identifier.of(TESMMod.MODID, "textures/gui/downarrow.png");
    public static boolean journalMenuOn = false;
    static int selectedQuest;
    static int[] color = new int[16];
    static int mouseRegion;
    static int selectedQuestPlus;
    static boolean journalTabState;
    final float MENU_UNDERLINE_H = 0.005f;
    final float MENU_SEPERATOR_H = 0.005f;

    final float QUESTLIST_SEPERATOR_W = 0.005f;
    int questListW;
    int[] questTabColor = {0xffffff,0xffffff};
    int journalStateMouseRegion = 0;

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void initExtended() {
        if(!journalMenuOn){ // 스크린 오픈 시에만 초기화
            journalMenuOn = true;
            journalTabState = false;
            selectedQuest = 1;
            Arrays.fill(color,0xffffff);
            selectedQuestPlus = 0;

        } // 해상도 변경 시에도 초기화
        questListW = (int)(width*((0.2025+0.145/2)-0.1)-(height*QUESTLIST_SEPERATOR_W/2));
        if(questListW % 2 == 1){
            questListW++;
        }
    }

    @Override
    public void close(){
        journalMenuOn = false;
        super.close();
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_J){
            this.close();
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_W){
            if(selectedQuestPlus > 0){
                selectedQuestPlus--;
            }
        }
        if(keyCode == GLFW.GLFW_KEY_S){
            if(selectedQuestPlus+16 < Quests.currentQuests.size()){
                selectedQuestPlus++;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (journalTabState){ // 완료 탭
                if (journalStateMouseRegion == 1) { // 진행중 클릭
                    journalTabState = false;
                    selectedQuest = 1;
                    selectedQuestPlus = 0;
                }
                if (mouseRegion != 0 && mouseRegion <= Quests.completedQuests.size()) {
                    selectedQuest = mouseRegion;
                }
            }else{ // 진행중 탭
                if (journalStateMouseRegion == 2) { // 완료 클릭
                    journalTabState = true;
                    selectedQuest = 1;
                    selectedQuestPlus = 0;
                }
                if (mouseRegion != 0 && mouseRegion <= Quests.currentQuests.size()) {
                    selectedQuest = mouseRegion;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

     public static boolean upArrowOn(){
        if(journalMenuOn){
            return selectedQuestPlus > 0;
        }
        return false;
    }

     public static boolean downArrowOn(){
        if(journalMenuOn){
            return selectedQuestPlus + 16 < Quests.currentQuests.size();
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context,mouseX,mouseY,delta);
        // 마우스 위치 및 관련 변수 업데이트
        getMouseRegion(mouseX,mouseY,0.25, questListW);
        // GUI 배경 출력
        context.drawTexture(GuiHelper::getGuiTexturedLayer, MENU_BACKGROUND,(int)(width*0.1),(int)(height*0.1),0,0,(int)(width*0.8),(int)(height*0.8) ,(int)(width*0.8),(int)(height*0.8));
        // 메뉴 탭 이름
        final String[] MENU_NAME = new String[]{"일지","기술","가방","지도"};
        for(int i=0; i<4; i++){
            // 메뉴 탭 밑줄
            context.drawTexture(GuiHelper::getGuiTexturedLayer, VER_LINE_AA9682,(int)(width*(0.2025+(0.15*i))),(int)(height*0.155),0,0,(int)(width*0.145),(int)(height* MENU_UNDERLINE_H), (int)(width*0.145),(int)(height* MENU_UNDERLINE_H));
            context.drawCenteredTextWithShadow(textRenderer, MENU_NAME[i],(int)(width*(0.2025+0.145/2 + 0.15*i)), (int)(height*0.115),0xffffff);
            // 메뉴 탭 - 내용 분리 칸막이
            context.drawTexture(GuiHelper::getGuiTexturedLayer, HOR_AAA685,(int)(width*0.1),(int)(height*(0.165-MENU_SEPERATOR_H/2)),0,0,(int)(width*0.8),(int)(height* MENU_SEPERATOR_H),(int)(width*0.8),(int)(height* MENU_SEPERATOR_H));
        }
        // 키 도움말 아이콘
        context.drawTexture(GuiHelper::getGuiTexturedLayer, Q_KEY_ICON,(int)(width*(0.20-0.015)-(height*0.02)),(int)(height*0.115),0,0,(int)(height*0.04),(int)(height*0.04),(int)(height*0.04),(int)(height*0.04));
        context.drawTexture(GuiHelper::getGuiTexturedLayer, E_KEY_ICON,(int)(width*(0.80+0.015)-(height*0.02)),(int)(height*0.115),0,0,(int)(height*0.04),(int)(height*0.04),(int)(height*0.04),(int)(height*0.04));
        // 퀘스트 목록 탭 색칠
        context.drawTexture(GuiHelper::getGuiTexturedLayer, QUESTLIST_TAB,(int)(width*0.1+(journalTabState?1:0)*(questListW/2)),(int)(height*(0.165+MENU_SEPERATOR_H/2)),0,0,(int)(questListW/2),(int)(height*(0.235-0.165-MENU_SEPERATOR_H)),(int)(questListW/2),(int)(height*(0.235-0.165-MENU_SEPERATOR_H)));
        // 퀘스트 탭 - 목록 칸막이
        context.drawTexture(GuiHelper::getGuiTexturedLayer, HOR_AAA685,(int)(width*0.1),(int)(height*(0.235-MENU_SEPERATOR_H/2)),0,0, questListW,(int)(height* MENU_SEPERATOR_H), questListW,(int)(height* MENU_SEPERATOR_H));

        if(journalTabState&&!Quests.completedQuests.isEmpty() || !journalTabState&&!Quests.currentQuests.isEmpty()){

            // 퀘스트 목록 - 선택된 퀘스트 배경
            if(selectedQuest>selectedQuestPlus && selectedQuest<=selectedQuestPlus+16){
                context.drawTexture(GuiHelper::getGuiTexturedLayer, TEXTURE_AAA685, (int)(width*0.1), getRenderingY(height*0.25,height*0.04,selectedQuest-1-selectedQuestPlus),0,0, questListW,(int)(height*0.04), questListW,(int)(height*0.04));
                context.drawTexture(GuiHelper::getGuiTexturedLayer, SELECTED_QUEST, (int)(width*(0.1+QUESTLIST_SEPERATOR_W/2))+questListW, getRenderingY(height*(0.25+(0.04/15)),height*0.04,selectedQuest-1-selectedQuestPlus),0,0,(int)(height*0.02*13/15),(int)(height*0.04*13/15),(int)(height*0.02*13/15),(int)(height*0.04*13/15));
            }
            // 퀘스트 목록 - 화살표
            if(upArrowOn()){
                context.drawTexture(GuiHelper::getGuiTexturedLayer, UP_ARROW, (int)(width*0.1+(questListW -height/24)/2),(int)(height*(0.22-0.01*(arrowState?1:0))),0,0,height/24,height/48,height/24,height/48);
            }
            if(downArrowOn()){
                context.drawTexture(GuiHelper::getGuiTexturedLayer, DOWN_ARROW, (int)(width*0.1+(questListW -height/24)/2),(int)(height*(0.9+0.01*(arrowState?1:0))),0,0,height/24,height/48,height/24,height/48);
            }
            // 퀘스트 목록 - 퀘스트 제목 출력 준비
            context.getMatrices().push();
            final float QUESTLIST_MAG = 0.7f;
            context.getMatrices().scale(QUESTLIST_MAG,QUESTLIST_MAG,1);

            // 퀘스트 목록 - 퀘스트 제목 출력
            for (int i = 0; i< getQuestListLength(); i++){
                context.drawText(textRenderer, getQuestNames(i),(int)(width*0.115/QUESTLIST_MAG), getRenderingY(height*0.255/QUESTLIST_MAG,height*0.04/QUESTLIST_MAG,i),color[i],false);
            }
            // 퀘스트 설명
            for(int i=0; i<getQuestDescription().split("\n").length; i++){
                context.drawText(textRenderer, getQuestDescription().split("\n")[i],
                        (int)(width*0.3/QUESTLIST_MAG), (int)(height*(0.3+(0.04*i))/QUESTLIST_MAG), 0xffffff,false);
            }
            // 퀘스트 목표
            int i=0;
            // 퀘스트 목표 - 완료한 목표
            for(int j = 0; j < getQuestObjective().split("\n").length-2; j++){
                context.drawText(textRenderer, "✓ " + getQuestObjective().split("\n")[j+1],
                        (int)(width*0.3/QUESTLIST_MAG),(int)(height*(0.85-(0.04* j))/QUESTLIST_MAG),0x808080,false);
                i++;
            }
            // 퀘스트 목표 - 현재 목표
            context.drawText(textRenderer, getQuestObjective().split("\n")[i+1],
                    (int)(width*0.3/QUESTLIST_MAG),(int)(height*(0.85-(0.04*i))/QUESTLIST_MAG),0xffffff,false);
            context.getMatrices().pop();
            // 퀘스트 제목
            context.drawCenteredTextWithShadow(textRenderer, getSelectedQuestName(),(int)(width*0.58), (int)(height*0.19), 0xffffff);
            context.drawTexture(GuiHelper::getGuiTexturedLayer, HOR_AAA685,(int)(width*0.36),(int)(height*(0.235-0.0025)),0,0,(int)(width*0.44),(int)(height*0.005),(int)(width*0.44),(int)(height*0.005));
            // 퀘스트 제목 양 옆 장식
            context.drawTexture(GuiHelper::getGuiTexturedLayer, QUESTBAR_SIDE,(int)(width*0.36-(height*0.015)),(int)(height*(0.235-0.015)),0,0,(int)(height*0.03),(int)(height*0.03),(int)(height*0.03),(int)(height*0.03));
            context.drawTexture(GuiHelper::getGuiTexturedLayer, QUESTBAR_SIDE,(int)(width*0.8-(height*0.015)),(int)(height*(0.235-0.015)),0,0,(int)(height*0.03),(int)(height*0.03),(int)(height*0.03),(int)(height*0.03));
        }

        // 퀘스트 목록 - 내용 칸막이
        context.drawTexture(GuiHelper::getGuiTexturedLayer, VER_AAA685,(int)(width*0.1 + questListW),(int)(height*(0.165+MENU_SEPERATOR_H/4)),0,0,(int)(height*QUESTLIST_SEPERATOR_W),(int)(height*(0.735-MENU_SEPERATOR_H)),(int)(height*QUESTLIST_SEPERATOR_W),(int)(height*(0.735-MENU_SEPERATOR_H)));
        // 퀘스트 목록 탭
        context.getMatrices().push();
        final float QUESTLIST_TAB_MAG = 0.85f;
        context.getMatrices().scale(QUESTLIST_TAB_MAG,QUESTLIST_TAB_MAG,1);
        // 퀘스트 목록 탭 이름
        // 0.275(퀘스트리스트 칸막이)와 0.1(배경 시작점)의 1/4 지점
        context.drawCenteredTextWithShadow(textRenderer,"진행중",(int)(width*(0.1+(0.275-0.1)/4)/QUESTLIST_TAB_MAG),(int)((height*0.185)/QUESTLIST_TAB_MAG), questTabColor[0]);
        // 0.275와 0.1의 3/4 지점
        context.drawCenteredTextWithShadow(textRenderer,"완료",(int)(width*(0.1+(0.275-0.1)*3/4)/QUESTLIST_TAB_MAG),(int)((height*0.185)/QUESTLIST_TAB_MAG),questTabColor[1]);
        context.getMatrices().pop();
    }

    // 출력할 퀘스트 갯수 설정
    private static int getQuestListLength() {
        if(journalTabState){ // 완료한 퀘스트 출력할 때
            if(Quests.completedQuests.size() > 16){
                return 16;
            } else{
                return Quests.completedQuests.size();
            }
        }else{ // 진행 중인 퀘스트 출력할 때
            if(Quests.currentQuests.size() > 16){
                return 16;
            } else{
                return Quests.currentQuests.size();
            }
        }
    }

    // 좌표 계산 시 소수점 문제를 해결하기 위한 메소드. 기준 좌표에 단순히 간격*i를 더해서 반환
    static int getRenderingY(double ref, double gap, int i){
        return (int)(ref + gap*i);
    }
    public void getMouseRegion(double mouseX, double mouseY, double yPosStart, double xPosEnd){
        if(journalMenuOn){
            // x좌표가 퀘스트 탭 내에 있을 때
            if(mouseX >= width*0.1 && mouseX < xPosEnd+width*0.1){
                // y좌표가 퀘스트 탭 내에 있을 때
                if(mouseY > height*(0.165-MENU_SEPERATOR_H/2) && mouseY < height*(0.235+MENU_SEPERATOR_H/2)){
                    Arrays.fill(color,0xffffff);
                    mouseRegion = 0;
                    journalStateMouseRegion = (mouseX < width * 0.1 + questListW / 2) ? 1:2;
                    if(journalStateMouseRegion == 1){
                        questTabColor[0] = 0xaaa685;
                        questTabColor[1] = 0xffffff;
                    }else {
                        questTabColor[0] = 0xffffff;
                        questTabColor[1] = 0xaaa685;
                    }
                    return;
                }else{ // y좌표가 퀘스트 탭 내에 있지 않을 때
                    for(int i=0; i<getQuestListLength(); i++){
                        // y좌표가 각 퀘스트 목록 영역에 해당하는지 검사
                        if(mouseY>=(int)(height*yPosStart) && mouseY< getRenderingY(height*yPosStart,height*0.04,i+1)){
                            // 퀘스트 제목 하나만 칠하고
                            Arrays.fill(color,0xffffff); // 나머지 제목과
                            Arrays.fill(questTabColor,0xffffff); // 퀘스트 탭은 흰색으로
                            if(i != selectedQuest-1-selectedQuestPlus){
                                color[i] = 0xAAA685;
                            }
                            mouseRegion = i+1+selectedQuestPlus;
                            return;
                        }
                    }
                }
            }
            // 어느 곳에도 걸리지 않는다면
            mouseRegion = 0;
            Arrays.fill(color,0xffffff);
            journalStateMouseRegion = 0;
            Arrays.fill(questTabColor,0xffffff);
        }
    }
    static String getQuestNames(int i){
        if(journalTabState){ // 완료한 퀘스트 목록 가져오기
            // 퀘스트 제목이 너무 길 때
            if(Quests.completedQuests.get(i+selectedQuestPlus)[0].length() > 12){
                return Quests.completedQuests.get(i+selectedQuestPlus)[0].substring(0,14)+"...";
            }else { // 일반적인 경우
                return Quests.completedQuests.get(i+selectedQuestPlus)[0];
            }
        }else { // 진행중인 퀘스트 목록 가져오기
            // 퀘스트 제목이 너무 길 때
            if(Quests.currentQuests.get(i+selectedQuestPlus)[0].length() > 12){
                return Quests.currentQuests.get(i+selectedQuestPlus)[0].substring(0,14)+"...";
            }else { // 일반적인 경우
                return Quests.currentQuests.get(i+selectedQuestPlus)[0];
            }
        }
    }
    static String getSelectedQuestName(){
        if(journalTabState){
            return Quests.completedQuests.get(selectedQuest-1)[0];
        }else{
            return Quests.currentQuests.get(selectedQuest-1)[0];
        }
    }
    static String getQuestDescription(){
        if(journalTabState){
            return Quests.completedQuests.get(selectedQuest-1)[1];
        }else{
            return Quests.currentQuests.get(selectedQuest-1)[1];
        }
    }
    static String getQuestObjective(){
        if(journalTabState){
            return Quests.completedQuests.get(selectedQuest-1)[2];
        }else{
            return Quests.currentQuests.get(selectedQuest-1)[2];
        }
    }
}