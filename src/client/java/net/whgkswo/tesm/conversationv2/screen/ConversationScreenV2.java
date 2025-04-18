package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiDirection;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.Box;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
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
    public void initExtended(){
        // 루트 컴포넌트 조정
        rootComponent.setChildrenHorizontalAlignment(HorizontalAlignment.LEFT);
        rootComponent.setChildrenVerticalAlignment(VerticalAlignment.CENTER);

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
        TextLabel partnerNameLabel = TextLabel.builder()
                .content(Text.literal(name))
                .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                .background(Box.builder()
                        .backgroundColor(TesmColor.WHITE)
                        .edgeColor(TesmColor.TRANSPARENT)
                        .bound(new RelativeBound(0.2, 0.2))
                        .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                        .childrenVerticalAlignment(VerticalAlignment.CENTER)
                        .axis(GuiDirection.VERTICAL)
                        .build())
                .id("partnerNameLabel")
                .build()
                .setParent(rootComponent);
        TextLabel partnerNameLabel2 = TextLabel.builder()
                .content(Text.literal(name + "2"))
                .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                .background(Box.builder()
                        .backgroundColor(TesmColor.RODEO_DUST)
                        .edgeColor(TesmColor.NEUTRAL_GOLD)
                        .bound(new RelativeBound(0.1, 0.1))
                        .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                        .childrenVerticalAlignment(VerticalAlignment.CENTER)
                        .axis(GuiDirection.VERTICAL)
                        .build())
                .id("partnerNameLabel2")
                .build()
                .setParent(rootComponent);
        TextLabel partnerNameLabel3 = TextLabel.builder()
                .content(Text.literal(name + "3"))
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .background(Box.builder()
                        .backgroundColor(TesmColor.NEUTRAL_GOLD)
                        .edgeColor(TesmColor.NEUTRAL_GOLD)
                        .bound(new RelativeBound(0.1, 0.1))
                        .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                        .childrenVerticalAlignment(VerticalAlignment.CENTER)
                        .axis(GuiDirection.VERTICAL)
                        .build())
                .id("partnerNameLabel3")
                .build()
                .setParent(rootComponent);
    }
}
