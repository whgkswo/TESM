package net.whgkswo.tesm.conversationv2.screen;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.*;
import net.whgkswo.tesm.conversationv2.event.DecisionHideEvent;
import net.whgkswo.tesm.conversationv2.event.DecisionRevealEvent;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.style.EdgeVisibility;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationScreenV2 extends TesmScreen {
    private String tempName;
    private String name;
    private String engName;
    private Flow currentFlow;
    private BoxPanel decisionContainer;
    private Map<TextLabel, DecisionV2> decisionMap = new HashMap<>();
    private TextLabel currentText;

    public ConversationScreenV2(ConversationNbtRes partnerInfo){
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
                .text(Text.literal(name))
                .id("partnerNameLabel")
                .topMarginRatio(0.6)
                .fontScale(2f)
                .build();
        // 대사 등록
        currentText = TextLabel.builder(container)
                .text(currentFlow.texts().poll().getText())
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
                .shouldHide(true)
                .build();
    }

    @Override
    public void close(){
        super.close();
        ConversationHelper.convOn = false;
    }

    private void advanceText(){
        if(!decisionContainer.isShouldHide()) return;

        // 대사 끝
        if(currentFlow.texts().isEmpty()){
            for (Action action : currentFlow.actions()){
                handleAction(action);
            }
        }else{ // 아직 남음
            DialogueText nextText = currentFlow.texts().poll();
            currentText.changeText(nextText.getText());
        }
    }

    private void handleAction(Action action){
        switch (action.type()){
            case SHOW_DECISIONS -> revealDecisions(action);
        }
    }

    private void revealDecisions(Action action){
        currentText.changeShouldHide(true);
        decisionContainer.removeChildren();
        decisionMap.clear();

        List<DecisionV2> decisions = ConversationHelper.getDecisions(action.target());

        for(int i = 0; i< decisions.size(); i++){
            // 뒷배경(호버 영역 확보용)
            BoxPanel background = BoxPanel.builder(decisionContainer)
                    .bound(1, 0.2)
                    .backgroundColor(TesmColor.TRANSPARENT)
                    .childrenVerticalAlignment(VerticalAlignment.CENTER)
                    .onHover(HoverType.BACKGROUND_BLUR_EFFECTER)
                    .build();
            // 텍스트
            DecisionV2 decision = decisions.get(i);
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
        decisionContainer.changeShouldHide(false);
    }

    private void hideDecision(){
        decisionContainer.changeShouldHide(true);
        currentText.changeShouldHide(false);
    }

    // 다음 플로우 로드
    private void advanceFlow(String flowId){
        currentFlow = ConversationHelper.getFlow(flowId);
        hideDecision();
        advanceText();
    }

    // 선택지 클릭 처리
    private void onClickDecision(TextLabel textLabel){
        DecisionV2 decision = decisionMap.get(textLabel);
        String nextFlowId = decision.flowId();

        if(nextFlowId == null || nextFlowId.isBlank()){
            // 대화 종료
            textLabel.getMotherScreen().close();
        }else{ // 다음 플로우 진행
            ((ConversationScreenV2)(textLabel.getMotherScreen())).advanceFlow(nextFlowId);
        }
    }
}
