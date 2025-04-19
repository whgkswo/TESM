package net.whgkswo.tesm.gui.component.elements.style;

import net.whgkswo.tesm.message.MessageHelper;

import java.lang.reflect.Field;

public record StylePreset<S extends GuiStyle>(String className, S style) {

    // 파라미터 스타일에 누락된 필드는 디폴트 스타일로 채워넣기
    public StylePreset{
        // 파라미터로 들어온 스타일의 타입 가져오기
        Class<S> styleClass = (Class<S>) style.getClass();

        // 해당 타입의 디폴트 스타일 가져오기
        StylePreset<S> defaultStylePreset = ((DefaultStyleProvider<S>)style).getDefaultStyle();

        if (defaultStylePreset != null) {
            // 필드 복사 로직 실행 - 리플렉션 사용
            try {
                S defaultStyle = defaultStylePreset.style();

                // 파라미터 스타일의 모든 필드 가져오기
                Field[] fields = styleClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);

                    // 현재 스타일의 필드 값 가져오기
                    Object styleValue = field.get(style);

                    // 값이 null이면 디폴트 스타일에서 가져와 덮어씌우기
                    if (styleValue == null) {
                        Object defaultValue = field.get(defaultStyle);
                        if (defaultValue != null) field.set(style, defaultValue);
                    }
                }
            } catch (Exception e) {
                // 예외 처리
                MessageHelper.sendMessage("스타일 필드 병합 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
