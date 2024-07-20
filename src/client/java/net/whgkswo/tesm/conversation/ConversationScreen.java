package net.whgkswo.tesm.conversation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.general.GlobalVariablesClient;
import net.whgkswo.tesm.gui.RenderUtil;
import net.whgkswo.tesm.networking.ModMessages;
import net.whgkswo.tesm.util.IEntityDataSaver;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static net.whgkswo.tesm.general.GlobalVariablesClient.arrowState;

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
    private String partnerName;
    private NpcDialogues partnerDL;
    private NormalStage currentDialogues;
    private DecisionStage currentDecisions;
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
        NbtCompound nbtCompound = ((IEntityDataSaver)partner).getPersistentData();
        String tempName = nbtCompound.getString("TempName");

        partnerName = tempName.isEmpty() ? nbtCompound.getString("Name") : tempName;

        partnerDL = GlobalVariablesClient.NPC_DIALOGUES.get(partnerName);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        // NPC 이름 출력
        renderPartnerName(context);
        // 대사 출력
        renderLine(context);
        // 선택지 출력
        renderDecisions(context);
        // 선택지 배경 출력
        if(decisionMakingOn){
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
        RenderUtil.renderText(RenderUtil.Alignment.CENTER,context,nameScale,partnerName,(int)(width/2/nameScale),(int)(height*0.55/nameScale),0xffffff);
    }
    private void renderLine(DrawContext context){
        currentDialogues = partnerDL.getNormalLines().get(stage);
        String content = decisionMakingOn ?
                lastLine : currentDialogues.getContents().get(currentLineIndex);

        RenderUtil.renderText(RenderUtil.Alignment.CENTER,context, LINE_SCALE,content,
                (int) (width/2/ LINE_SCALE),(int) (height*0.7/ LINE_SCALE),0xffffff);
    }
    private void renderDecisions(DrawContext context){
        currentDecisions = partnerDL.getDecisions().get(stage);
        if(decisionMakingOn){
            int endIndex;
            if(currentDecisions.getContents().size() >= MAX_DISPLAY_DC){
                endIndex = decisionOffset + MAX_DISPLAY_DC;
            }else{ // 선택지가 4개 미만
                endIndex = currentDecisions.getContents().size();
            }
            // 출력
            for(int i = decisionOffset; i< endIndex; i++){
                RenderUtil.renderText(RenderUtil.Alignment.LEFT,context, LINE_SCALE,currentDecisions.getContents().get(i).getContent(),
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
        if(currentDecisions.getContents().size() >= MAX_DISPLAY_DC){
            iMax = MAX_DISPLAY_DC;
        }else{ // 선택지가 4개 미만
            iMax = currentDecisions.getContents().size();
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
                    currentLineIndex++;
                }else{ // 현재 스테이지를 종료하며
                    switch (currentDialogues.getExecuteAfter()){
                        case SHOW_DECISIONS -> {
                            lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1);
                            decisionMakingOn = true;
                        }
                        case JUMP_TO -> {
                            lastLine = currentDialogues.getContents().get(currentDialogues.getContents().size()-1);
                            setStage(currentDialogues.getExecuteTarget());
                            decisionMakingOn = true;
                        }
                        case EXIT -> {
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
}



