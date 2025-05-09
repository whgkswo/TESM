package net.whgkswo.tesm.conversation.screen;

import net.minecraft.text.Text;
import net.whgkswo.tesm.conversation.ConversationHelper;
import net.whgkswo.tesm.conversation.Flow;
import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.colors.TesmColor;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.bounds.RelativeBound;
import net.whgkswo.tesm.gui.component.components.BoxPanel;
import net.whgkswo.tesm.gui.component.components.TextLabel;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.features.base.ClickHandler;
import net.whgkswo.tesm.gui.component.components.style.BoxStyle;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.message.MessageHelper;
import net.whgkswo.tesm.networking.payload.data.s2c_res.ConversationNbtRes;

public class ExampleScreen extends TesmScreen {
    private String tempName;
    private String name;
    private String engName;
    private Flow currentFlow;

    public ExampleScreen(ConversationNbtRes partnerInfo){
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

        BoxPanel boxPanel = BoxPanel.builder(rootComponent)
                .id("box")
                .bound(new RelativeBound(0.2, 0.2))
                .selfHorizontalAlignment(HorizontalAlignment.CENTER)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .childrenVerticalAlignment(VerticalAlignment.CENTER)
                .backgroundColor(TesmColor.RODEO_DUST)
                .edgeColor(TesmColor.CREAM/*.withAlpha(100)*/)
                .edgeThickness(1)
                .horizontalGap(0.1)
                .verticalGap(0.05)
                .onHover(HoverType.BACKGROUND_BLUR_EFFECTER)
                .onClick(ClickHandler.of(this::close))
                .isScrollable(true)
                .build();

        BoxPanel boxPanel2 = BoxPanel.builder(boxPanel)
                .id("box2")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.RED)
                /*.topMarginRatio(0.05f)
                .bottomMarginRatio(0.05f)*/
                .build();

        BoxPanel boxPanel3 = BoxPanel.builder(boxPanel)
                .id("box3")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.ORANGE)
                /*.topMarginRatio(0.05f)
                .bottomMarginRatio(0.05f)*/
                .build();

        BoxPanel boxPanel4 = BoxPanel.builder(boxPanel)
                .id("box4")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.YELLOW)
                .topMarginRatio(0.05f)
                .bottomMarginRatio(0.05f)
                .build();

        BoxPanel boxPanel5 = BoxPanel.builder(boxPanel)
                .id("box5")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.GREEN)
                /*.topMarginRatio(0.05f)
                .bottomMarginRatio(0.05f)*/
                .build();

        BoxPanel boxPanel6 = BoxPanel.builder(boxPanel)
                .id("box6")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.BLUE)
                /*.topMarginRatio(0.05f)
                .bottomMarginRatio(0.05f)*/
                .build();

        BoxPanel boxPanelB = BoxPanel.builder(rootComponent)
                .childrenHorizontalAlignment(HorizontalAlignment.CENTER)
                .id("box2b")
                .stylePreset(BoxStyle.TEST)
                .edgeColor(TesmColor.CREAM/*.withAlpha(100)*/)
                .edgeThickness(10)
                .build();

        TextLabel textLabel = TextLabel.builder(boxPanelB)
                //.fontScale(0.5f)
                .text(Text.literal("텍스트1"))
                .backgroundColor(TesmColor.GREEN)
                .sizeMode(TextLabel.SizeMode.RELATIVE_TO_PARENT)
                .selfHorizontalAlignment(HorizontalAlignment.RIGHT)
                .build();

        TextLabel textLabel2 = TextLabel.builder(boxPanel)
                .fontScale(2f)
                .backgroundColor(TesmColor.GREEN)
                .text(Text.literal("텍스트2"))
                .onClick(() -> MessageHelper.sendMessage("클릭!"))
                .build();

        TextLabel textLabel3 = TextLabel.builder(boxPanel)
                .fontScale(2f)
                .text(Text.literal("텍스트3"))
                .build();

        TextLabel textLabel4 = TextLabel.builder(boxPanel)
                .fontScale(2f)
                .text(Text.literal("텍스트4"))
                .build();

        TextLabel textLabel5 = TextLabel.builder(boxPanel)
                .fontScale(2f)
                .text(Text.literal("텍스트5"))
                .build();

        BoxPanel boxPanelA = BoxPanel.builder(boxPanel)
                .id("boxA")
                .bound(new RelativeBound(0.8, 0.2))
                .backgroundColor(TesmColor.YELLOW)
                .positionType(PositionType.FIXED)
                .build();
    }
}
