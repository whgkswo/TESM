package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.GuiDirection;
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
        rootComponent.setChildrenHorizontalAlignment(HorizontalAlignment.CENTER);
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
                .text(Text.literal(name))
                .id("text1")
                .build()
                .setParent(rootComponent);

        Box box = Box.builder()
                .id("box")
                .bound(new RelativeBound(0.2, 0.2))
                .backgroundColor(TesmColor.RODEO_DUST.withAlpha(100))
                .build()
                .setParent(rootComponent);

        TextLabel partnerNameLabel2 = TextLabel.builder()
                .text(Text.literal(name).withColor(TesmColor.RODEO_DUST.getHexDecimalCode()))
                .id("text2")
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .selfVerticalAlignment(VerticalAlignment.LOWER)
                .build()
                .setParent(box);
    }
}
