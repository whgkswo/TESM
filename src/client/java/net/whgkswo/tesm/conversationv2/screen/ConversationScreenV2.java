package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.elements.BoxPanel;
import net.whgkswo.tesm.gui.component.elements.TextLabel;
import net.whgkswo.tesm.gui.component.elements.style.BoxStyle;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.templete.TesmScreen;
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
                .parent(rootComponent)
                .text(Text.literal(name))
                .id("text1")
                .build();

        BoxPanel boxPanel = BoxPanel.builder()
                .parent(rootComponent)
                .id("box")
                .bound(new RelativeBound(0.2, 0.2))
                .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                .backgroundColor(TesmColor.RODEO_DUST.withAlpha(100))
                .build();

        TextLabel partnerNameLabel2 = TextLabel.builder()
                .parent(boxPanel)
                .text(Text.literal(name))
                .id("text2")
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .selfVerticalAlignment(VerticalAlignment.LOWER)
                .shadowed(true)
                .fontScale(2.4f)
                .build();

        TextLabel partnerNameLabel3 = TextLabel.builder()
                .parent(boxPanel)
                .text(Text.literal(name))
                .id("text3")
                .fontScale(2.1f)
                .selfHorizontalAlignment(HorizontalAlignment.LEFT)
                .selfVerticalAlignment(VerticalAlignment.LOWER)
                .build();

        TextLabel partnerNameLabel4 = TextLabel.builder()
                .parent(boxPanel)
                .text(Text.literal(name))
                .id("text4")
                .fontScale(1.8f)
                .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                .selfVerticalAlignment(VerticalAlignment.LOWER)
                .build();

        BoxPanel boxPanel2 = BoxPanel.builder()
                .parent(rootComponent)
                .id("box2")
                .stylePreset(BoxStyle.TEST)
                .build();
    }
}
