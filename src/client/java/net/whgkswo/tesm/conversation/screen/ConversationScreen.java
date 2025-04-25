package net.whgkswo.tesm.conversation.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversation.*;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.style.EdgeVisibility;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationScreen extends TesmScreen {
    private String tempName;
    private String name;
    private String engName;
    private Flow currentFlow;
    private BoxPanel decisionContainer;
    private Map<TextLabel, Decision> decisionMap = new HashMap<>();
    private TextLabel currentText;
    private NameType partnerNameType = NameType.KOR;
    private BoxPanel logContainer;

    public ConversationScreen(ConversationNbtRes partnerInfo){
        super();
        this.tempName = partnerInfo.tempName();
        this.name = partnerInfo.name();
        this.engName = partnerInfo.engName();
        super.shouldRenderBackground(false);
    }

    @Override
    public void initExtended(){
        ConversationHelper.convOn = true;
        // 루트 컴포넌트 조정
        rootComponent.setChildrenHorizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.onClick(this::advanceText);

        // 컨테이너 등록
        BoxPanel container = BoxPanel.builder(rootComponent)
                .bound(0.6, 1)
                .id("container")
                .backgroundColor(TesmColor.TRANSPARENT)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .build();

        currentFlow = ConversationHelper.getFlow(engName, "general");
        // NPC 이름 등록
        TextLabel partnerNameLabel = TextLabel.builder(container)
                .text(Text.literal(getPartnerDisplayName()))
                .id("partnerNameLabel")
                .topMarginRatio(0.6)
                .fontScale(2f)
                .build();
        // 대사 등록
        currentText = TextLabel.builder(container)
                .text(Text.literal(""))
                .topMarginRatio(0.1)
                .fontScale(1.1f)
                .id("currentText")
                .build();
        // 선택지 컨테이너 등록
        decisionContainer = BoxPanel.builder(container)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeColor(TesmColor.CREAM)
                .edgeThickness(3)
                .edgeVisibility(EdgeVisibility.LEFT_ONLY)
                .topMarginRatio(0.05)
                .verticalGap(0.035)
                .isScrollable(true)
                .id("decision_container")
                .bound(1, 0.2)
                .visibility(false)
                .build();
        rootModal.setBound(new RelativeBound(0.7, 0.8));
        // 로그 모달 내부 컨테이너
        logContainer = BoxPanel.builder(rootModal)
                .id("log_container")
                .bound(0.9, 0.9)
                .backgroundColor(TesmColor.TRANSPARENT)
                .isScrollable(true)
                .verticalGap(0.02)
                .build();
        // 단축키 표시
        TextLabel logKeyLabel = TextLabel.builder(rootComponent)
                .text("[X] 로그")
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .selfVerticalAlignment(VerticalAlignment.BOTTOM)
                .onClick(this::toggleLogModal)
                .build();
        // 첫 대사 로드
        advanceText();
    }

    @Override
    public void close(){
        super.close();
        ConversationHelper.convOn = false;
    }

    private String getPartnerDisplayName(){
        if(partnerNameType.equals(NameType.TEMP)){
            return tempName;
        } else if (partnerNameType.equals(NameType.KOR)) {
            return name;
        }
        return "(이름 타입 없음)";
    }

    private void advanceText(){
        if(!currentText.isVisible()) return;

        // 대사 끝
        if(currentFlow.texts().isEmpty()){
            for (Action action : currentFlow.actions()){
                handleAction(action);
            }
        }else{ // 아직 남음
            DialogueText nextText = currentFlow.texts().poll();
            currentText.changeText(nextText.getText());
            // 로그 기록
            addPartnerLog(getPartnerDisplayName(), nextText.getText());
        }
    }

    private void addPartnerLog(String partnerDisplayName, Text text){
        int number = logContainer.getChildren().size() + 1;
        BoxPanel logBubble = BoxPanel.builder(logContainer)
                .id("log_bubble#" + number)
                .bound(1, 0.08)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeVisibility(EdgeVisibility.LEFT_ONLY)
                .edgeThickness(3)
                .childrenVerticalAlignment(VerticalAlignment.CENTER)
                .build();

        TextLabel speakerLabel = TextLabel.builder(logBubble)
                .id("log_speacker#" + number)
                .text(Text.literal(partnerDisplayName).withColor(0xFF8C00))
                .leftMarginRatio(0.02)
                .build();
        TextLabel logTextLabel = TextLabel.builder(logBubble)
                .text(text)
                .id("log_text#" + number)
                .leftMarginRatio(0.02)
                .build();
    }

    private void addPlayerLog(Text text){
        int number = logContainer.getChildren().size() + 1;

        BoxPanel logBubble = BoxPanel.builder(logContainer)
                .id("log_bubble#" + number)
                .bound(1, 0.1)
                .backgroundColor(TesmColor.TRANSPARENT)
                .edgeVisibility(EdgeVisibility.RIGHT_ONLY)
                .edgeThickness(3)
                .childrenVerticalAlignment(VerticalAlignment.CENTER)
                .build();

        TextLabel logTextLabel = TextLabel.builder(logBubble)
                .text(text.copy().withColor(0x6395EE))
                .id("log_player_text#" + number)
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .selfVerticalAlignment(VerticalAlignment.CENTER)
                .rightMarginRatio(0.02)
                .build();
    }

    private void handleAction(Action action){
        switch (action.type()){
            case SHOW_DECISIONS -> revealDecisions(action);
        }
    }

    private void revealDecisions(Action action){
        currentText.changeVisibility(false);
        decisionContainer.removeChildren();
        decisionMap.clear();

        List<Decision> decisions = ConversationHelper.getDecisions(action.target());

        for(int i = 0; i< decisions.size(); i++){
            // 뒷배경(호버 영역 확보용)
            BoxPanel background = BoxPanel.builder(decisionContainer)
                    .bound(1, 0.2)
                    .backgroundColor(TesmColor.TRANSPARENT)
                    .childrenVerticalAlignment(VerticalAlignment.CENTER)
                    .onHover(HoverType.BACKGROUND_BLUR_EFFECTER)
                    .build();
            // 텍스트
            Decision decision = decisions.get(i);
            TextLabel textLabel = TextLabel.builder(background)
                    .text(Text.literal(decision.text()))
                    .id("decision_slot#" + (i + 1))
                    .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                    .leftMarginRatio(0.025)
                    .build();
            // 선택지 클릭 이벤트 설정
            background.onClick(() -> onClickDecision(textLabel));
            // 맵에 등록
            decisionMap.put(textLabel, decision);
        }
        decisionContainer.changeVisibility(true);
    }

    private void hideDecision(){
        decisionContainer.changeVisibility(false);
        currentText.changeVisibility(true);
    }

    // 다음 플로우 로드
    private void advanceFlow(String flowId){
        currentFlow = ConversationHelper.getFlow(flowId);
        hideDecision();
        advanceText();
    }

    // 선택지 클릭 처리
    private void onClickDecision(TextLabel textLabel){
        Decision decision = decisionMap.get(textLabel);
        String nextFlowId = decision.flowId();
        // 로그 기록
        addPlayerLog(decision.getText());

        if(nextFlowId == null || nextFlowId.isBlank()){
            // 대화 종료
            textLabel.getMotherScreen().close();
        }else{ // 다음 플로우 진행
            ((ConversationScreen)(textLabel.getMotherScreen())).advanceFlow(nextFlowId);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        switch (keyCode){
            case GLFW.GLFW_KEY_X -> {
                toggleLogModal();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void toggleLogModal(){
        rootModal.setVisibility(!rootModal.isVisible());
        rootModal.clearCaches();
    }
}
