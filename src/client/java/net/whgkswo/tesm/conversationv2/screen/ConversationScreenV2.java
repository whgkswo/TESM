package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

public class ConversationScreenV2 extends TesmScreen {
    private String tempName;
    private String name;
    private String engName;
    private Flow currentFlow;

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
        rootComponent.setChildrenVerticalAlignment(VerticalAlignment.CENTER);

        currentFlow = ConversationHelper.getFlow(engName, "general");
        // NPC 이름 등록
        addPartnerNameComponent();
        // 대사 출력
        addDialogueText();
        /*if(decisionMakingOn){
            // 선택지 출력
            renderDecisions(context);
            // 선택지 배경 출력
            renderDecisionBackground(context,mouseY);
        }*/
    }

    @Override
    public void close(){
        super.close();
        ConversationHelper.convOn = false;
    }

    private void addPartnerNameComponent(){
        TextLabel partnerNameLabel = TextLabel.builder()
                .parent(rootComponent)
                .text(Text.literal(name))
                .id("partnerNameLabel")
                .fontScale(2.5f)
                .build();
    }

    private void addDialogueText(){
        TextLabel currentText = TextLabel.builder()
                .text(currentFlow.texts().peek().getText())
                .parent(rootComponent)
                .build();

        BoxPanel box = BoxPanel.builder()
                .id("box")
                .bound(new RelativeBound(0.2, 0.2))
                .parent(rootComponent)
                .build();

        BoxPanel box2 = BoxPanel.builder()
                .id("box2")
                .bound(new RelativeBound(0.3, 0.2))
                .backgroundColor(TesmColor.YELLOW)
                .parent(box)
                .build();

        TextLabel currentText3 = TextLabel.builder()
                .text(currentFlow.texts().peek().getText())
                .parent(box)
                .id("text3")
                .build();
    }
}
