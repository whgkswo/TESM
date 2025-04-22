package net.whgkswo.tesm.conversationv2.screen;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.*;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.message.MessageHelper;
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

        currentFlow = ConversationHelper.getFlow(engName, "general");
        // NPC 이름 등록
        addPartnerNameComponent();
        // 대사 등록
        addDialogueText();
        // 선택지 등록
        addDecisions();
    }

    @Override
    public void close(){
        super.close();
        ConversationHelper.convOn = false;
    }

    private void addPartnerNameComponent(){
        TextLabel partnerNameLabel = TextLabel.builder(rootComponent)
                .text(Text.literal(name))
                .id("partnerNameLabel")
                .topMarginRatio(0.6f)
                .fontScale(2f)
                .onClick(this::next)
                .build();
    }

    private void addDialogueText(){
        TextLabel currentText = TextLabel.builder(rootComponent)
                .text(currentFlow.texts().poll().getText())
                .topMarginRatio(0.1f)
                .id("currentText")
                .onClick(this::next)
                .build();
    }

    private void addDecisions(){
        decisionContainer = new DecisionContainer();
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
            this.close();
            MessageHelper.sendMessage("선택지 로드 실패");
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

        public DecisionContainer(){
            for (int i = 0; i< MAX_VISIBLE_DECISIONS; i++){
                TextLabel slot = TextLabel.builder(rootComponent)
                        .text(Text.of(""))
                        .id("decision_slot#" + (i + 1))
                        .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                        .shouldHide(true)
                        .build();
                decisionSlots.add(slot);
            }
        }

        private void revealDecision(List<DecisionV2> decisions){
            this.decisions = decisions;
            for(int i = offset; i< Math.min(offset + MAX_VISIBLE_DECISIONS, decisions.size()); i++){
                TextLabel slot = decisionSlots.get(i - offset);
                slot.setText(decisions.get(i).getText());
                slot.setShouldHide(false);
            }
            shouldDecisionRevealed = true;
            clearAllCachedBounds();
        }
    }
}
