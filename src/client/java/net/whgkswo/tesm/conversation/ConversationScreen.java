package net.whgkswo.tesm.conversation;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.helpers.GuiHelper;
import net.whgkswo.tesm.gui.screen.templete.CustomScreen;
import net.whgkswo.tesm.networking.payload.data.c2s_req.SetNbtReq;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static net.whgkswo.tesm.general.GlobalVariablesClient.*;

@Environment(EnvType.CLIENT)
public class ConversationScreen extends CustomScreen {
    private static final int MAX_DISPLAY_DC = 4;
    private List<Integer> colors = new ArrayList<>();
    private static final float LINE_SCALE = 0.8f;
    private Entity partner;
    private String stage = "General";
    private String lastLine;
    private int currentLineIndex = 0;
    private boolean decisionMakingOn;
    private int decisionOffset;
    private String partnerDisplayName;
    private NpcDialogues partnerDL;
    private NormalStage currentDialogues;
    private DecisionStage currentDecisions;
    private List<AvailableDecision> availableDecisions;
    private Decision selectedDecision;
    private static final Identifier ARROW_UP = Identifier.of(TESMMod.MODID, "textures/gui/uparrow.png");
    private static final Identifier ARROW_DOWN = Identifier.of(TESMMod.MODID, "textures/gui/downarrow.png");
    private final Identifier DECISION_BACKGROUND = Identifier.of(TESMMod.MODID, "textures/gui/decision.png");
    public ConversationScreen(Entity partner){
        super();
        this.partner = partner;
        ConversationStart.convOn = true;
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){
    }

    @Override
    public void init(){
        super.init();
        partnerDisplayName = convPartnerTempName.isEmpty() ? convPartnerName : convPartnerTempName;
        partnerDL = GlobalVariablesClient.NPC_DIALOGUES.get(convPartnerName);
    }

    @Override
    public void close(){
        super.close();
        ConversationStart.convOn = false;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        // NPC 이름 출력
        renderPartnerName(context);
        // 대사 출력
        renderLine(context);
        if(decisionMakingOn){
            // 선택지 출력
            renderDecisions(context);
            // 선택지 배경 출력
            renderDecisionBackground(context,mouseY);
        }
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB){
            this.close();
            return true;
            // W키 (리스트 올리기)
        } else if (keyCode == GLFW.GLFW_KEY_W) {
            onScrollUp();
            // S키 (리스트 내리기)
        } else if (keyCode == GLFW.GLFW_KEY_S) {
            onScrollDown();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public void onScrollUp(){
        if(decisionOffset > 0){
            decisionOffset--;
        }
    }
    @Override
    public void onScrollDown(){
        if (decisionOffset < availableDecisions.size()-MAX_DISPLAY_DC){
            decisionOffset++;
        }
    }
    private void renderPartnerName(DrawContext context){
        final float nameScale = 1.5f;
        RenderingHelper.renderText(Alignment.CENTER,context,nameScale, partnerDisplayName,(int)(width/2/nameScale),(int)(height*0.55/nameScale),0xffffff);
    }
    private void renderLine(DrawContext context){
        currentDialogues = partnerDL.getNormalLines().get(stage);
        String content = decisionMakingOn ?
                lastLine : currentDialogues.getContents().get(currentLineIndex).getLine();

        RenderingHelper.renderText(Alignment.CENTER,context, LINE_SCALE,content,
                (int) (width/2/ LINE_SCALE),(int) (height*0.7/ LINE_SCALE),0xffffff);
    }
    private int getEndIndexOfDisplayDecisions(){
        int endIndex;
        if(availableDecisions.size() >= MAX_DISPLAY_DC){
            endIndex = decisionOffset + MAX_DISPLAY_DC;
        }else{ // 선택지가 4개 미만
            endIndex = availableDecisions.size();
        }
        return endIndex;
    }
    private void renderDecisions(DrawContext context){
        // 출력할 선택지 끝 번호 구하기
        int endIndex = getEndIndexOfDisplayDecisions();
        // 가용한 선택지가 하나도 없으면 대화 종료
        if(endIndex == 0){
            close();
        }
        // 출력
        for(int i = decisionOffset; i< endIndex; i++){
            RenderingHelper.renderText(Alignment.LEFT,context, LINE_SCALE,availableDecisions.get(i).getDecision().getLine(),
                    (int)(width*0.15/ LINE_SCALE),
                    (int)(height*(0.78+0.04*(i-decisionOffset))/ LINE_SCALE),
                    colors.get(i));
        }
        // 선택지 화살표 출력
        if (upArrowOn()){
            context.drawTexture(GuiHelper::getLayer, ARROW_UP, (int)(width*0.2), (int)(height*(0.745-0.01*(arrowState?1:0))), 0, 0, height/24,height/48,height/24,height/48);
        }
        if (downArrowOn(availableDecisions.size())) {
            context.drawTexture(GuiHelper::getLayer, ARROW_DOWN, (int)(width*0.2), (int)(height*(0.94+0.01*(arrowState?1:0)+1/48)), 0, 0, height/24,height/48,height/24,height/48);
        }
    }
    private List<AvailableDecision> getAvailableDecisions(){
        List<Decision> decisions = currentDecisions.getContents();
        List<AvailableDecision> availableDecision = new ArrayList<>();

        for(int i = 0; i< decisions.size(); i++){
            Decision decision = decisions.get(i);
            if(decision.getQuestRequirement() == null){
                availableDecision.add(new AvailableDecision(decision,i));
            }else{
                String requiredQuestName = decision.getQuestRequirement().getQuestName();
                QuestStatus requiredQuestStatus = decision.getQuestRequirement().getStatus();
                // 조건에 맞는 선택지만 필터링
                Quest requiredQuest = Quest.QUESTS.get(requiredQuestName);
                if(requiredQuest.getStatus() == requiredQuestStatus){
                    // 진행중인 퀘스트는 스테이지까지 검사
                    if(requiredQuestStatus == QuestStatus.ONGOING){
                        String requiredStage = requiredQuest.getCurrentStage();
                        if(!requiredStage.equals(decision.getQuestRequirement().getStage())){
                            continue;
                        }
                    }
                    availableDecision.add(new AvailableDecision(decision, i));
                }
            }
        }
        return availableDecision;
    }
    private void renderDecisionBackground(DrawContext context, int mouseY){
        resetColors();
        MouseArea mouseArea = getMouseArea(mouseY);
        if(mouseArea != MouseArea.REST_AREA){
            int areaNumber = mouseArea.getNumber();
            if(currentDecisions.getContents().size() >= areaNumber){
                RenderingHelper.renderTexture(context, DECISION_BACKGROUND, 0.12, 0.775+0.04*(areaNumber-1),0.5,0.04);
                colors.set(decisionOffset + areaNumber-1,0xAAA685);
            }
        }
    }
    private void resetColors(){
        colors.clear();
        for(int i = 0; i< availableDecisions.size(); i++){
            boolean isChosen = availableDecisions.get(i).getDecision().isChosen();
            colors.add(isChosen ? 0x9b9b9b : 0xffffff);
        }
    }
    private boolean upArrowOn() {
        // 위 화살표 활성화
        if (decisionMakingOn){
            return decisionOffset > 0;
        }
        return false;
    }
    private boolean downArrowOn(int maxDecisions){
        // 아래 화살표 활성화
        if (decisionMakingOn){
            return decisionOffset + MAX_DISPLAY_DC < maxDecisions;
        }
        return false;
    }
    private MouseArea getMouseArea(double mouseY){
        int iMax;
        // 선택지가 4개 이상
        if(availableDecisions.size() >= MAX_DISPLAY_DC){
            iMax = MAX_DISPLAY_DC;
        }else{ // 선택지가 4개 미만
            iMax = availableDecisions.size();
        }
        for(int i = 0; i< iMax; i++){
            if(mouseY >= height*(0.78+0.04*i) && mouseY<height*(0.82+0.04*i)){
                return MouseArea.getMouseArea(i+1);
            }
        }
        return MouseArea.REST_AREA;
    }
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT){
            if(!decisionMakingOn){ // 일반 대사 넘기기
                if(currentLineIndex < currentDialogues.getContents().size()-1){
                    getNextLine();
                }else{ // 현재 스테이지를 종료하며
                    executeAfter();
                }
            }else{ // 선택지 클릭
                onClickDecision(mouseY);
            }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    private void getNextLine(){
        // 이름을 밝히는 라인일 경우
        if(currentDialogues.getContents().get(currentLineIndex).isRevealName()){
            if(partnerDisplayName.equals(convPartnerTempName)){
                revealPartnerName();
            }
        }
        currentLineIndex++;
    }
    private void executeAfter(){
        switch (currentDialogues.getExecuteAfter()){
            case SHOW_DECISIONS -> {
                lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
                showDecisions();
            }
            case JUMP_TO -> {
                lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
                setStage(currentDialogues.getExecuteTarget());
                showDecisions();
            }
            case START_QUEST -> {
                Quest.startQuest(currentDialogues.getExecuteTarget());
                resetStageAfterRecieveQuest();
            }
            case ADVANCE_QUEST -> {
                Quest.advanceQuest(currentDialogues.getExecuteTarget(), selectedDecision);
                resetStageAfterRecieveQuest();
            }
            case COMPLETE_QUEST -> {
                Quest.completeQuest(currentDialogues.getExecuteTarget());
                resetStageAfterRecieveQuest();
            }
            case CLOSE -> {
                close();
            }
            default -> {
                close();
            }
        }
    }
    private void onClickDecision(double mouseY){
        MouseArea mouseArea = getMouseArea(mouseY);
        if(mouseArea != MouseArea.REST_AREA){
            int selectedDC = mouseArea.getNumber() + decisionOffset - 1;
            selectedDC = availableDecisions.get(selectedDC).getIndex();
            decisionMakingOn = false;
            // 선택지 선택 표시
            selectedDecision = currentDecisions.getContents().get(selectedDC);
            selectedDecision.setChosen(true);
            // 클릭한 선택지에 해당하는 대사 출력 준비
            setStage(stage + "-" + (selectedDC + 1));
        }
    }
    private void showDecisions(){
        decisionMakingOn = true;
        currentDecisions = partnerDL.getDecisions().get(stage);
        availableDecisions = getAvailableDecisions();
        if(decisionOffset > availableDecisions.size() - Math.min(MAX_DISPLAY_DC, availableDecisions.size())){
            decisionOffset --;
        }
        resetColors();
    }
    private void setStage(String stage){
        this.stage = stage;
        currentLineIndex = 0;
    }
    private void resetStageAfterRecieveQuest(){
        lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
        setStage(stage.substring(0, stage.length() - 2));
        showDecisions();
    }
    private void revealPartnerName(){
        partnerDisplayName = convPartnerName;

        Map<String, Object> data = new HashMap<>();
        data.put("TempName", "");

        ClientPlayNetworking.send(new SetNbtReq(partner.getId(), data));
    }

    private enum MouseArea {
        AREA_1(1),
        AREA_2(2),
        AREA_3(3),
        AREA_4(4),
        REST_AREA(0)
        ;
        private int number;
        MouseArea(int number) {
            this.number = number;
        }
        private int getNumber(){
            return number;
        }
        private static MouseArea getMouseArea(int number){
            switch (number){
                case 1:
                    return AREA_1;
                case 2:
                    return AREA_2;
                case 3:
                    return AREA_3;
                case 4:
                    return AREA_4;
                default:
                    return REST_AREA;
            }
        }
    }
    private class AvailableDecision{
        private Decision decision;
        private int index;

        public AvailableDecision(Decision decision, int index) {
            this.decision = decision;
            this.index = index;
        }

        public Decision getDecision() {
            return decision;
        }

        public int getIndex() {
            return index;
        }
    }
}



