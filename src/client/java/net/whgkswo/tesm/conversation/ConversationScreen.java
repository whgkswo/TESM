package net.whgkswo.tesm.conversation;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.general.OnClientTicks;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.npcs.NpcDialogues;
import net.whgkswo.tesm.npcs.quests.Quests;
import org.lwjgl.glfw.GLFW;


import java.util.ArrayList;
import java.util.Arrays;

import static net.whgkswo.tesm.conversation.ConversationStart.*;

@Environment(EnvType.CLIENT)
public class ConversationScreen extends Screen {
    public ConversationScreen() {
        super(Text.literal("테스트 화면입니다."));
    }
    private static final int MAX_DISPLAY_DC = 4;
    private final Identifier DECISION_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/decision.png");
    private final Identifier ARROW_UP = new Identifier(TESMMod.MODID, "textures/gui/uparrow.png");
    private final Identifier ARROW_DOWN = new Identifier(TESMMod.MODID, "textures/gui/downarrow.png");
    static int currentLine = 1;
    static boolean decisionMakingOn = false;
    static String lastLine = "";

    static int[] color = {0xffffff,0xffffff,0xffffff,0xffffff};

    static String currentStage = "1";
    static int decisionPlus;
    public static ArrayList<String[]> currentDCs = new ArrayList<String[]>(){};

    //TODO
    // - NPC 종족별 이름 색상


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static boolean upArrowOn() {
        // 위 화살표 활성화
        if (decisionMakingOn){
            return decisionPlus > 0;
        }
        return false;
    }
    public static boolean downArrowOn(){
        // 아래 화살표 활성화
        if (decisionMakingOn){
            return decisionPlus + MAX_DISPLAY_DC < currentDCs.size();
        }
        return false;
    }

    // 마우스 위치 식별
    public int getMouseRegion(double mouseY) {
        if(decisionMakingOn){
            int iMax;
            if(currentDCs.size() >= MAX_DISPLAY_DC){ // 선택지가 4개 이상이면
               iMax = MAX_DISPLAY_DC;
            }else{ // 선택지가 4개 미만
                iMax = currentDCs.size();
            }
            // 선택지 색상 선정
            for(int i = 0; i< iMax; i++){
                if(mouseY>=height*(0.78+0.04*i) && mouseY<height*(0.82+(0.04*i))){
                    setColor(iMax); // 전체 색상 초기화
                    color[i] = 0xAAA685; // 마우스 올린 선택지 색상만 금색
                    return i+1;
                }
            } // 마우스가 어디에도 속하지 않으면 전체 색상 초기화
            setColor(iMax);
        }
        return 0;
    }

    // 닫기 (TAB)
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB){
            this.close();
            return true;
            // W키 (리스트 올리기)
        } else if (keyCode == GLFW.GLFW_KEY_W) {
            if(decisionPlus > 0){
                decisionPlus--;
            }
            // S키 (리스트 내리기)
        } else if (keyCode == GLFW.GLFW_KEY_S) {
            if (decisionPlus < currentDCs.size()-MAX_DISPLAY_DC){
                decisionPlus++;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    // 대사 넘기기 (왼클릭)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (!decisionMakingOn){ // 일반 대사 넘기기
                if (getMouseRegion(mouseY) == 0){
                    if(currentLine < getDLArray()[1].length){
                        currentLine++;
                    }else{ // 현재 다이얼로그를 종료하면서:
                        switch(getDLArray()[0][0]){
                            case "선택형": // 해당하는 선택지 출력
                                getCurrentDCs();
                                if(currentDCs.isEmpty()){
                                    close();
                                }else{
                                    lastLine = getDLArray()[1][currentLine-1];
                                    decisionPlus = 0;
                                    decisionMakingOn = true;
                                }
                                break;
                            case "회귀형": // 이전 선택지로 돌아감
                                lastLine = getDLArray()[1][currentLine-1]; // getDLArray()가 currentStage를 참조하므로 위치에 주의!!
                                currentStage = getDLArray()[0][1];
                                afterDLFinished();
                                break;
                            case "임무형":
                                if(getDLArray()[0][1].equals("시작")) {
                                    Quests.startQuest(getDLArray()[0][2]);
                                }else if(getDLArray()[0][1].equals("완료")){
                                    Quests.clearQuest(getDLArray()[0][2]);
                                }
                                close();
                                break;
                            case "종료형": // 대화 종료
                                close();
                                break;
                        }
                    }
                }
            }else{ // 선택지 클릭 시
                if (getMouseRegion(mouseY)!=0){
                    int selectedDC = getMouseRegion(mouseY)-1; //TODO: 왜인지 변수를 거치지 않고 currentDCs.get(getMouseRegion)을 바로 쓰면 팅김
                    decisionMakingOn = false;
                    // 선택지 선택 표시
                    NpcDialogues.npcDLMap.get(convPartnerName+"_선택지_"+currentStage)[selectedDC+decisionPlus][2] = "선택";
                    // 클릭한 선택지에 맞는 번호의 대사를 출력할 준비
                    currentStage = currentStage + "-" + currentDCs.get(selectedDC + decisionPlus)[0];
                    currentLine = 1;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context,mouseX,mouseY,delta);
        final float CP_NAME_MAG = 1.5f;
        final float DL_MAG = 0.8f;
        // NPC 이름 출력
        context.getMatrices().push();
        context.getMatrices().scale(1.5f,1.5f,1);
        context.drawCenteredTextWithShadow(textRenderer, convPartnerName,(int) (width/2/CP_NAME_MAG),(int) (height*0.55/CP_NAME_MAG),0xffffff);
        context.getMatrices().pop();
        // 대사 출력
        context.getMatrices().push();
        context.getMatrices().scale(0.8f,0.8f,1);
        context.drawCenteredTextWithShadow(textRenderer, getDLLine(),(int) (width/2/DL_MAG),(int) (height*0.7/DL_MAG),0xffffff);
        // 선택지 출력
        if(decisionMakingOn){
            if(currentDCs.size() >= MAX_DISPLAY_DC){
                for(int i = decisionPlus; i< decisionPlus + MAX_DISPLAY_DC; i++){
                    context.drawTextWithShadow(textRenderer,
                            getDLArray()[i][1], // 텍스트
                            (int) (width*0.15/DL_MAG), // x위치
                            (int) (height*(0.78+0.04*(i- decisionPlus))/DL_MAG), // y위치
                            color[i-decisionPlus]); // 색상
                }
            }else{ // 선택지가 4개 미만
                for(int i = decisionPlus; i< currentDCs.size(); i++){
                    context.drawTextWithShadow(textRenderer,
                            getDLArray()[i][1], // 텍스트
                            (int) (width*0.15/DL_MAG), // x위치
                            (int) (height*(0.78+0.04*(i- decisionPlus))/DL_MAG), // y위치
                            color[i-decisionPlus]); // 색상
                }
            }
        }
        context.getMatrices().pop();

        // 선택지 배경 출력
        if(getMouseRegion(mouseY)!=0){
            int mouseOverDC = getMouseRegion(mouseY);
            if(currentDCs.size()>=mouseOverDC){
                RenderSystem.enableBlend();
                context.drawTexture(DECISION_BACKGROUND,(int) (width*0.12),(int) (height*(0.775+0.04*(mouseOverDC-1))),0,0,220,(int) (height*0.04),220,15);
                }
            }
        // 선택지 화살표 출력
        if (upArrowOn()){
            context.drawTexture(ARROW_UP, (int)(width*0.2), (int)(height*(0.745-0.01*(GlobalVariablesClient.arrowState?1:0))), 0, 0, height/24,height/48,height/24,height/48);
        }
        if (downArrowOn()) {
            context.drawTexture(ARROW_DOWN, (int)(width*0.2), (int)(height*(0.94+0.01*(GlobalVariablesClient.arrowState?1:0)+1/48)), 0, 0, height/24,height/48,height/24,height/48);
        }
    }

    // 스크린 열릴 때 실행
    @Override
    protected void init(){
        if(!convOn){
            convOn = true;
            currentLine = 1;
            decisionMakingOn = false;
            currentStage = "1";
            currentDCs.clear();
            decisionPlus = 0;
        }
    }

    // 스크린 닫을 때 실행
    @Override
    public void close(){
        // 대화 상대 움직임 제한 해제 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(convPartner.getId());
        ClientPlayNetworking.send(ModMessages.UNFREEZE_ENTITY_ID, buf);

        convOn = false;
        currentStage = "1"; // init()에서도 currentStage를 초기화하지만 스크린이 열리기 전, 대화 가능 npc 판별 시점에서 getDLArray가 호출되고 이 때 currentStage가 사용되므로 초기화가 필요함
        super.close();
    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ ↓ 대사 가져오기 ↓ ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    // 배열 째로 가져와서
    public static String[][] getDLArray(){
        if(decisionMakingOn) {
            return NpcDialogues.npcDLMap.get(convPartnerName + "_선택지_" + currentStage);
        }else{
            return NpcDialogues.npcDLMap.get(convPartnerName + "_대사_" + currentStage);
        }
    }

    // 개별 문자열 추출
    static String getDLLine(){
        if(decisionMakingOn){
            return lastLine;
        }else{
            return getDLArray()[1][currentLine-1];
        }
    }
    // 다이얼로그 종료 후 실행
    static void afterDLFinished(){
        decisionPlus = 0;
        decisionMakingOn = true; // getDLArray에서 decisionMakingOn을 참조하므로 위치에 주의!!
        getCurrentDCs();
    }
    // 전체 선택지 중 화면에 표시할 선택지만 선별
    static void getCurrentDCs(){
        currentDCs.clear();
        // 선택지 문자열들을 하나씩 가져와서 검사
        for(String[] dLList : NpcDialogues.npcDLMap.get(convPartnerName+"_선택지_"+currentStage)){
            // 조건 없는 선택지
            if(dLList[0].isEmpty()){
                // 현재 선택지 목록 생성
                currentDCs.add(new String[]{dLList[3],dLList[2]});
            }else{ // 조건이 있는 선택지
                switch (dLList[0].split(":")[1]){
                    case "시작 가능":
                        if(Quests.availableQuests.containsKey(dLList[0].split(":")[0])){
                            currentDCs.add(new String[]{dLList[3],dLList[2]});
                        }
                        break;
                    case "진행중":
                        if(Quests.currneQuestsMap.containsKey(dLList[0].split(":")[0])){
                            currentDCs.add(new String[]{dLList[3],dLList[2]});
                        }
                        break;
                    case "완료됨":
                        if(Quests.completedQuestsMap.containsKey(dLList[0].split(":")[0])){
                            currentDCs.add(new String[]{dLList[3],dLList[2]});
                        }
                        break;
                }
            }
        }
    }
    void setColor(int iMax){
        for (int j=0; j<iMax;j++){
            if(currentDCs.get(j+decisionPlus)[1] == "선택"){
                color[j] = 0x9b9b9b; // 155,155,155
            }else if(currentDCs.get(j+decisionPlus)[1] == "미선택"){
                color[j] = 0xffffff; // 255,255,255
            }
        }
    }
}

