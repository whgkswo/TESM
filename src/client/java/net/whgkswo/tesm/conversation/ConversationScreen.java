package net.whgkswo.tesm.conversation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.conversation.quest.Quest;
import net.whgkswo.tesm.conversation.quest.QuestStatus;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.gui.RenderUtil;
import net.whgkswo.tesm.networking.ModMessages;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static net.whgkswo.tesm.general.GlobalVariablesClient.*;

@Environment(EnvType.CLIENT)
public class ConversationScreen extends Screen {
    private static final int MAX_DISPLAY_DC = 4;
    private List<Integer> colors = new ArrayList<>(List.of(0xffffff, 0xffffff, 0xffffff, 0xffffff));
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
    private static final Identifier ARROW_UP = new Identifier(TESMMod.MODID, "textures/gui/uparrow.png");
    private static final Identifier ARROW_DOWN = new Identifier(TESMMod.MODID, "textures/gui/downarrow.png");
    private final Identifier DECISION_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/decision.png");
    public ConversationScreen(Entity partner){
        super(Text.literal("대화 시스템 GUI"));
        this.partner = partner;
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta){
    }
    @Override
    public boolean shouldPause(){
        return false;
    }
    @Override
    protected void init(){
        partnerDisplayName = convPartnerTempName.isEmpty() ? convPartnerName : convPartnerTempName;
        partnerDL = GlobalVariablesClient.NPC_DIALOGUES.get(convPartnerName);
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
            if(decisionOffset > 0){
                decisionOffset--;
            }
            // S키 (리스트 내리기)
        } else if (keyCode == GLFW.GLFW_KEY_S) {
            if (decisionOffset < currentDecisions.getContents().size()-MAX_DISPLAY_DC){
                decisionOffset++;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public void close(){
        // 대화 상대 움직임 제한 해제 (서버에 패킷 전송)
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(partner.getId());
        ClientPlayNetworking.send(ModMessages.UNFREEZE_ENTITY_ID,buf);
        super.close();
    }
    private void renderPartnerName(DrawContext context){
        final float nameScale = 1.5f;
        RenderUtil.renderText(RenderUtil.Alignment.CENTER,context,nameScale, partnerDisplayName,(int)(width/2/nameScale),(int)(height*0.55/nameScale),0xffffff);
    }
    private void renderLine(DrawContext context){
        currentDialogues = partnerDL.getNormalLines().get(stage);
        String content = decisionMakingOn ?
                lastLine : currentDialogues.getContents().get(currentLineIndex).getLine();

        RenderUtil.renderText(RenderUtil.Alignment.CENTER,context, LINE_SCALE,content,
                (int) (width/2/ LINE_SCALE),(int) (height*0.7/ LINE_SCALE),0xffffff);
    }
    private void renderDecisions(DrawContext context){
        currentDecisions = partnerDL.getDecisions().get(stage);
        availableDecisions = getAvailableDecisions();
        int endIndex;
        if(availableDecisions.size() >= MAX_DISPLAY_DC){
            endIndex = decisionOffset + MAX_DISPLAY_DC;
        }else{ // 선택지가 4개 미만
            endIndex = availableDecisions.size();
        }
        // 가용한 선택지가 하나도 없으면 대화 종료
        if(endIndex == 0){
            close();
        }
        // 출력
        for(int i = decisionOffset; i< endIndex; i++){
            RenderUtil.renderText(RenderUtil.Alignment.LEFT,context, LINE_SCALE,availableDecisions.get(i).getDecision().getLine(),
                    (int)(width*0.15/ LINE_SCALE),
                    (int)(height*(0.78+0.04*(i-decisionOffset))/ LINE_SCALE),
                    colors.get(i));
        }
        // 선택지 화살표 출력
        if (upArrowOn()){
            context.drawTexture(ARROW_UP, (int)(width*0.2), (int)(height*(0.745-0.01*(arrowState?1:0))), 0, 0, height/24,height/48,height/24,height/48);
        }
        if (downArrowOn(currentDecisions.getContents().size())) {
            context.drawTexture(ARROW_DOWN, (int)(width*0.2), (int)(height*(0.94+0.01*(arrowState?1:0)+1/48)), 0, 0, height/24,height/48,height/24,height/48);
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
                if(Quest.QUESTS.get(requiredQuestName).getStatus() == requiredQuestStatus){
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
                RenderUtil.renderTexture(context, DECISION_BACKGROUND, (int) (width*0.12), (int) (height*(0.775+0.04*(areaNumber-1))),220,(int) (height*0.04));
                colors.set(areaNumber-1,0xAAA685);
            }
        }
    }
    private void resetColors(){
        int amount = Math.min(MAX_DISPLAY_DC, currentDecisions.getContents().size());
        for(int i = decisionOffset; i< decisionOffset + amount; i++){
            boolean isChosen = currentDecisions.getContents().get(i).isChosen();
            colors.set(i, isChosen ? 0x9b9b9b : 0xffffff);
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
                    // 이름을 밝히는 라인일 경우
                    if(currentDialogues.getContents().get(currentLineIndex).isRevealName()){
                        if(partnerDisplayName.equals(convPartnerTempName)){
                            revealPartnerName();
                        }
                    }
                    currentLineIndex++;
                }else{ // 현재 스테이지를 종료하며
                    switch (currentDialogues.getExecuteAfter()){
                        case SHOW_DECISIONS -> {
                            lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
                            decisionMakingOn = true;
                        }
                        case JUMP_TO -> {
                            lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
                            setStage(currentDialogues.getExecuteTarget());
                            decisionMakingOn = true;
                        }
                        case START_QUEST -> {
                            String questName = currentDialogues.getExecuteTarget();
                            Quest quest = Quest.QUESTS.get(questName);
                            quest.setStatus(QuestStatus.ONGOING);
                            GlobalVariables.player.sendMessage(Text.literal(String.format("시작: %s", questName)));
                            resetStageAfterRecieveQuest();
                        }
                        case COMPLETE_QUEST -> {
                            String questName = currentDialogues.getExecuteTarget();
                            Quest quest = Quest.QUESTS.get(questName);
                            quest.setStatus(QuestStatus.COMPLETED);
                            GlobalVariables.player.sendMessage(Text.literal(String.format("완료: %s", questName)));
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
            }else{ // 선택지 클릭
                MouseArea mouseArea = getMouseArea(mouseY);
                if(mouseArea != MouseArea.REST_AREA){
                    int selectedDC = mouseArea.getNumber();
                    decisionMakingOn = false;
                    // 선택지 선택 표시
                    currentDecisions.getContents().get(selectedDC-1).setChosen(true);
                    // 클릭한 선택지에 해당하는 대사 출력 준비
                    setStage(stage + "-" + selectedDC);
                }
            }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    private void setStage(String stage){
        this.stage = stage;
        currentLineIndex = 0;
    }
    private void resetStageAfterRecieveQuest(){
        lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1).getLine();
        setStage(stage.substring(0, stage.length() - 2));
        decisionMakingOn = true;
    }
    private void revealPartnerName(){
        partnerDisplayName = convPartnerName;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(partner.getId());
        ClientPlayNetworking.send(ModMessages.UPDATE_NBT, buf);
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



