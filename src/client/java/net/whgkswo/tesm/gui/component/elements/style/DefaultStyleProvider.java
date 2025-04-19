package net.whgkswo.tesm.gui.component.elements.style;

import net.whgkswo.tesm.message.MessageHelper;

public interface DefaultStyleProvider<S extends GuiStyle> {
    StylePreset<S> getDefaultStyle();

    static <S extends GuiStyle> StylePreset<S> getDefaultStyle(Class<S> styleType) {
        try{
            // 더미 인스턴스 생성
            S dummyInstance = styleType.getDeclaredConstructor().newInstance();
            if(dummyInstance instanceof DefaultStyleProvider<?>){
                return ((DefaultStyleProvider<S>) dummyInstance).getDefaultStyle();
            }
        } catch (NoSuchMethodException e) {
            // 이 메서드를 사용하는 클래스는 반드시 기본 생성자를 가지고 있어야 함
            MessageHelper.sendMessage(styleType.getName() + "클래스에 기본 생성자가 없습니다.");
        } catch (Exception e){
            MessageHelper.sendMessage("Gui스타일 더미 인스턴스 생성 실패");
        }
        return null;
    }
}
