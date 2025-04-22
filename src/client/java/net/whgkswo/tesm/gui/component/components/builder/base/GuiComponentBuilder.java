package net.whgkswo.tesm.gui.component.components.builder.base;

import net.whgkswo.tesm.gui.HorizontalAlignment;
import net.whgkswo.tesm.gui.component.GuiComponent;
import net.whgkswo.tesm.gui.component.ParentComponent;
import net.whgkswo.tesm.gui.component.bounds.providers.FixedPositionProvider;
import net.whgkswo.tesm.gui.component.bounds.providers.FlowPositionProvider;
import net.whgkswo.tesm.gui.component.bounds.PositionType;
import net.whgkswo.tesm.gui.component.components.features.HoverType;
import net.whgkswo.tesm.gui.component.components.features.base.ClickHandler;
import net.whgkswo.tesm.gui.component.components.features.base.HoverHandler;
import net.whgkswo.tesm.gui.component.components.style.GuiStyle;
import net.whgkswo.tesm.gui.component.components.style.StylePreset;
import net.whgkswo.tesm.gui.exceptions.GuiException;
import net.whgkswo.tesm.gui.screen.VerticalAlignment;
import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.lang.LanguageHelper;

import java.util.Set;

// 원시타입 스타일 초기값은 여기에 명시
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
    protected HoverType hoverType;
    protected HoverHandler hoverHandler;
    protected ClickHandler clickHandler;

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

    public B onHover(HoverType hoverType){
        this.hoverType = hoverType;
        return self();
    }

    public B onHover(HoverHandler hoverHandler){
        this.hoverHandler = hoverHandler;
        return self();
    }

    public B onClick(ClickHandler clickHandler){
        this.clickHandler = clickHandler;
        return self();
    }

    public B onClick(Runnable onClick){
        this.clickHandler = ClickHandler.of(onClick);
        return self();
    }

    protected C buildExtended(C c){
        //TODO: 이거 왜 있는거임? setParent는 하위 빌더에서 처리 하는데, 크로스인가
        //component.setParent(parent);

        // 기본 ID 설정
        if(id == null || id.isBlank()){
            String type = c.getClass().getSimpleName();
            String snakeType = LanguageHelper.toSnakeCase(type);
            String code = LanguageHelper.generateRandomCode(8);
            c.setId(snakeType + "#" + code);
        }
        // 식별자 중복 검사
        if(!c.getId().equals(TesmScreen.ROOT_ID) && c.getMotherScreen() != null) {
            Set<String> componentIdSet = c.getMotherScreen().getComponentIdSet();
            if(componentIdSet.contains(c.getId())){
                new GuiException(c.getMotherScreen(), "컴포넌트 식별자가 중복되었습니다: " + c.getId()).handle();
            }else{
                componentIdSet.add(c.getId());
            }
        }

        // 기본 포지션 정책은 플로우
        switch (this.positionType){
            case FIXED -> c.setPositionProvider(new FixedPositionProvider(c, parent));
            default -> c.setPositionProvider(new FlowPositionProvider(c, parent));
        };

        // 스타일 초기화
        c.initializeStyle();
        return c;
    }
}
