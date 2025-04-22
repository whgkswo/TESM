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
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
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
    private DecisionContainer decisionContainer;

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
                .topMarginRatio(0.6f)
                .fontScale(2f)
                .build();
        // 대사 등록
        TextLabel currentText = TextLabel.builder(container)
                .text(currentFlow.texts().poll().getText())
                .topMarginRatio(0.1f)
                .fontScale(1.1f)
                .id("currentText")
                .build();
        // 선택지 등록
        decisionContainer = new DecisionContainer(container);
    }

    @Override
    public void close(){
        super.close();
        ConversationHelper.convOn = false;
    }

    private void advanceText(){
        TextLabel currentText = (TextLabel) searchComponent("currentText");

        if(currentFlow.texts().isEmpty()){
            for (Action action : currentFlow.actions()){
                handleAction(action);
            }
        }else{
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
        if(decisionContainer.isShouldDecisionRevealed()) return;
        try{
            TextLabel currentText = (TextLabel) searchComponent("currentText");
            currentText.changeShouldHide(true);
            List<DecisionV2> decisions = ConversationHelper.getDecisions(action.target());

            decisionContainer.revealDecision(decisions);
        } catch (RuntimeException e) {
            new GuiException(this, "선택지 로드 실패");
        }
    }

    // 다음 플로우 로드
    private void advanceFlow(String flowId){
        currentFlow = ConversationHelper.getFlow(flowId);
        decisionContainer.hideDecision();
    }

    @Getter
    public class DecisionContainer {
        public static final int MAX_VISIBLE_DECISIONS = 4;
        private List<DecisionV2> decisions = new ArrayList<>();
        private final List<TextLabel> textLabels = new ArrayList<>();
        private final Map<TextLabel, DecisionV2> decisionMap = new HashMap<>();
        @Setter
        private int offset;
        private boolean shouldDecisionRevealed;
        private ConversationScreenV2 motherScreen;

        public DecisionContainer(ParentComponent<?,?> parent){
            for (int i = 0; i< MAX_VISIBLE_DECISIONS; i++){
                double topMargin = i == 0 ? 0.05 : 0;
                // 뒷배경(호버 영역 확보용)
                BoxPanel background = BoxPanel.builder(parent)
                        .bound(1, 0.05)
                        .backgroundColor(TesmColor.TRANSPARENT)
                        .childrenVerticalAlignment(VerticalAlignment.CENTER)
                        .topMarginRatio(topMargin)
                        .onHover(HoverType.BACKGROUND_BLUR_EFFECTER)
                        .shouldHide(true)
                        .build();
                // 텍스트
                TextLabel textLabel = TextLabel.builder(background)
                        .text(Text.of(""))
                        .id("decision_slot#" + (i + 1))
                        .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                        .leftMarginRatio(0.02)
                        .build();
                textLabels.add(textLabel);
                // 스크린 연결
                motherScreen = (ConversationScreenV2) parent.getMotherScreen();
                // 선택지 클릭 이벤트 등록
                background.onClick(() -> onClickDecision(textLabel));
            }
        }

        // 선택지 클릭 처리
        private void onClickDecision(TextLabel textLabel){
            DecisionV2 decision = getDecisionFromSlot(textLabel);
            String nextFlowId = decision.flowId();

            if(nextFlowId == null || nextFlowId.isBlank()){
                // 대화 종료
                textLabel.getMotherScreen().close();
            }else{ // 다음 플로우 진행
                ((ConversationScreenV2)(textLabel.getMotherScreen())).advanceFlow(nextFlowId);
            }
        }

        private DecisionV2 getDecisionFromSlot(TextLabel textLabel){
            return decisionMap.get(textLabel);
        }

        // 선택지 밝히기
        private void revealDecision(List<DecisionV2> decisions){
            this.decisions = decisions;
            for(int i = offset; i< Math.min(offset + MAX_VISIBLE_DECISIONS, decisions.size()); i++){
                TextLabel textLabel = textLabels.get(i - offset);
                decisionMap.put(textLabel, decisions.get(i));
                applyDecisionToSlot(textLabel);

                BoxPanel slotArea = (BoxPanel) textLabel.getParent();
                slotArea.setShouldHide(false);
            }
            clearAllCachedBounds();
        }

        // 선택지 감추기
        private void hideDecision(){
            for (TextLabel textLabel : textLabels){
                textLabel.getParent().changeShouldHide(true);
            }
            TextLabel currentText = (TextLabel) searchComponent("currentText");
            currentText.setShouldHide(false);
            advanceText();
            clearAllCachedBounds();
        }

        private void applyDecisionToSlot(TextLabel textLabel){
            DecisionV2 decision = decisionMap.get(textLabel);
            textLabel.setText(decision.getText());
        }
    }
}
