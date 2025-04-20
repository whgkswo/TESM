package net.whgkswo.tesm.conversationv2.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversationv2.ConversationHelper;
import net.whgkswo.tesm.conversationv2.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
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
        /*TextLabel partnerNameLabel = TextLabel.builder()
                .parent(rootComponent)
                .text(Text.literal(name))
                .id("text1")
                .build();*/

        BoxPanel boxPanel = BoxPanel.builder()
                .parent(rootComponent)
                .id("box")
                .bound(new RelativeBound(0.2, 0.2))
                .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .childrenVerticalAlignment(VerticalAlignment.CENTER)
                .backgroundColor(TesmColor.RODEO_DUST.withAlpha(100))
                .edgeColor(TesmColor.CREAM)
                .edgeThickness(10)
                .build();

        BoxPanel boxPanel2 = BoxPanel.builder()
                .parent(boxPanel)
                .id("box2")
                .bound(new RelativeBound(0.8, 0.25))
                .backgroundColor(TesmColor.RED)
                .build();

        BoxPanel boxPanel3 = BoxPanel.builder()
                .parent(boxPanel)
                .id("box3")
                .bound(new RelativeBound(0.8, 0.25))
                .backgroundColor(TesmColor.ORANGE)
                .build();

        BoxPanel boxPanel4 = BoxPanel.builder()
                .parent(boxPanel)
                .id("box4")
                .bound(new RelativeBound(0.8, 0.25))
                .backgroundColor(TesmColor.YELLOW)
                .build();

        BoxPanel boxPanel5 = BoxPanel.builder()
                .parent(boxPanel)
                .id("box5")
                .bound(new RelativeBound(0.8, 0.25))
                .backgroundColor(TesmColor.GREEN)
                .build();

        BoxPanel boxPanel6 = BoxPanel.builder()
                .parent(boxPanel)
                .id("box5")
                .bound(new RelativeBound(0.8, 0.25))
                .backgroundColor(TesmColor.BLUE)
                .build();

        BoxPanel boxPanelB = BoxPanel.builder()
                .parent(rootComponent)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .id("box2")
                .stylePreset(BoxStyle.TEST)
                .build();

        TextLabel textLabel = TextLabel.builder()
                .parent(boxPanelB)
                .fontScale(2f)
                .text(Text.literal("텍스트1"))
                .build();

        TextLabel textLabel2 = TextLabel.builder()
                .parent(boxPanelB)
                .fontScale(2f)
                .text(Text.literal("텍스트2"))
                .build();

        TextLabel textLabel3 = TextLabel.builder()
                .parent(boxPanelB)
                .fontScale(2f)
                .text(Text.literal("텍스트3"))
                .build();

        TextLabel textLabel4 = TextLabel.builder()
                .parent(boxPanelB)
                .fontScale(2f)
                .text(Text.literal("텍스트4"))
                .build();

        TextLabel textLabel5 = TextLabel.builder()
                .parent(boxPanelB)
                .fontScale(2f)
                .text(Text.literal("텍스트5"))
                .build();
    }
}
