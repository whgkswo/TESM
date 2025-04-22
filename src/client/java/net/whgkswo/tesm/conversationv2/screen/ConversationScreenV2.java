package net.whgkswo.tesm.conversationv2.screen;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.*;
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
import java.util.List;

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
        rootComponent.onClick(this::next);

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

    private void next(){
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

    @Getter
    public class DecisionContainer {
        public static final int MAX_VISIBLE_DECISIONS = 4;
        private List<DecisionV2> decisions = new ArrayList<>();
        private final List<TextLabel> decisionSlots = new ArrayList<>();
        @Setter
        private int offset;
        private boolean shouldDecisionRevealed;

        public DecisionContainer(ParentComponent<?,?> parent){
            for (int i = 0; i< MAX_VISIBLE_DECISIONS; i++){
                double topMargin = i == 0 ? 0.05 : 0;
                BoxPanel slotArea = BoxPanel.builder(parent)
                        .bound(1, 0.05)
                        .backgroundColor(TesmColor.TRANSPARENT)
                        .childrenVerticalAlignment(VerticalAlignment.CENTER)
                        .topMarginRatio(topMargin)
                        .onHover(HoverType.BACKGROUND_BLUR_EFFECTER)
                        .shouldHide(true)
                        .build();
                TextLabel slot = TextLabel.builder(slotArea)
                        .text(Text.of(""))
                        .id("decision_slot#" + (i + 1))
                        .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                        .leftMarginRatio(0.02)
                        .build();
                decisionSlots.add(slot);
            }
        }

        private void revealDecision(List<DecisionV2> decisions){
            this.decisions = decisions;
            for(int i = offset; i< Math.min(offset + MAX_VISIBLE_DECISIONS, decisions.size()); i++){
                TextLabel slot = decisionSlots.get(i - offset);
                slot.setText(decisions.get(i).getText());
                BoxPanel slotArea = (BoxPanel) slot.getParent();
                slotArea.setShouldHide(false);
            }
            shouldDecisionRevealed = true;
            clearAllCachedBounds();
        }
    }
}
