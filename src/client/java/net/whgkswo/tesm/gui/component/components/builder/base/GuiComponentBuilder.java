package net.whgkswo.tesm.gui.component.components.builder.base;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.providers.FixedPositionProvider;
import net.whgkswo.tesm.gui.component.bounds.providers.FlowPositionProvider;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.component.components.style.StylePreset;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.lang.LanguageHelper;

public abstract class GuiComponentBuilder<C extends GuiComponent<C, S>,
        B extends GuiComponentBuilder<C, B, S>,
        S extends GuiStyle>
        implements ComponentBuilder<C, B> {

    // GuiComponent 필드
    protected String id;
    protected boolean shouldHide;
    protected HorizontalAlignment selfHorizontalAlignment;
    protected VerticalAlignment selfVerticalAlignment;
    protected ParentComponent<?, ?> parent;
    protected StylePreset<S> stylePreset;
    protected PositionType positionType = PositionType.FLOW;
    protected double topMarginRatio;
    protected double bottomMarginRatio;
    protected double leftMarginRatio;
    protected double rightMarginRatio;

    public B id(String id) {
        this.id = id;
        return self();
    }

    public B shouldHide(boolean shouldHide) {
        this.shouldHide = shouldHide;
        return self();
    }

    public B selfHorizontalAlignment(HorizontalAlignment selfHorizontalAlignment) {
        this.selfHorizontalAlignment = selfHorizontalAlignment;
        return self();
    }

    public B selfVerticalAlignment(VerticalAlignment selfVerticalAlignment) {
        this.selfVerticalAlignment = selfVerticalAlignment;
        return self();
    }

    public B parent(ParentComponent<?, ?> parent) {
        this.parent = parent;
        return self();
    }

    public B stylePreset(StylePreset<S> stylePreset) {
        this.stylePreset = stylePreset;
        return self();
    }

    public B positionType(PositionType positionType) {
        this.positionType = positionType;
        return self();
    }

    public B topMarginRatio(double topMarginRatio){
        this.topMarginRatio = topMarginRatio;
        return self();
    }

    public B bottomMarginRatio(double bottomMarginRatio){
        this.bottomMarginRatio = bottomMarginRatio;
        return self();
    }

    public B leftMarginRatio(double leftMarginRatio){
        this.leftMarginRatio = leftMarginRatio;
        return self();
    }

    public B rightMarginRatio(double rightMarginRatio){
        this.rightMarginRatio = rightMarginRatio;
        return self();
    }

    protected C register(C component){
        component.setParent(parent);

        // 기본 ID 설정
        if(id == null || id.isBlank()){
            String type = component.getClass().getSimpleName();
            String snakeType = LanguageHelper.toSnakeCase(type);
            String code = LanguageHelper.generateRandomCode(8);
            component.setId(snakeType + "#" + code);
        }

        // 기본 포지션 정책은 플로우
        switch (this.positionType){
            case FIXED -> component.setPositionProvider(new FixedPositionProvider(component, parent));
            default -> component.setPositionProvider(new FlowPositionProvider(component, parent));
        }

        // 스타일 초기화
        component.initializeStyle();
        return component;
    }
}
