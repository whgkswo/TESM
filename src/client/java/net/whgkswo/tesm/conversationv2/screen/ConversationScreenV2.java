package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiDirection;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.EdgedBox;
import net.whgkswo.tesm.gui.component.elements.FilledBox;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.screen.templete.TesmScreen;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

public class ConversationScreenV2 extends TesmScreen {
    private String tempName;
    private String name;
    private String engName;
    private Flow currentFlow;

    public ConversationScreenV2(ConversationNbtRes partnerInfo){
        super(GuiDirection.VERTICAL);
        this.tempName = partnerInfo.tempName();
        this.name = partnerInfo.name();
        this.engName = partnerInfo.engName();
        super.shouldRenderBackground(false);
    }

    @Override
    public void init(){
        currentFlow = ConversationHelper.getFlow(engName, "general");
        // NPC 이름 등록
        addPartnerNameComponent();
        // 대사 출력
        /*renderLine(context);
        if(decisionMakingOn){
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
        TextLabel partnerNameLabel = new TextLabel(
                Text.literal(name),
                EdgedBox.builder()
                        .backgroundColor(TesmColor.WHITE)
                        .edgeColor(TesmColor.TRANSPARENT)
                        .bound(new RectangularBound(Boundary.BoundType.FLEXIBLE, 0, 0, 0.5, 0.5))
                        .childrenAlignment(HorizontalAlignment.CENTER)
                        .thickness(1)
                        .axis(GuiDirection.VERTICAL)
                        .build(),


                /*new FilledBox(
                        rootComponent,
                        new RectangularBound(Boundary.BoundType.FLEXIBLE, 0, 0, 0.5, 0.5),
                        TesmColor.WHITE
                ),*/
                1.0f,
                HorizontalAlignment.CENTER,
                0
        );
        rootComponent.addChild(partnerNameLabel);
    }
}
