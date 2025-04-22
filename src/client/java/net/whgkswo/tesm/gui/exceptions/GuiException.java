package net.whgkswo.tesm.gui.exceptions;

import net.whgkswo.tesm.gui.screen.base.TesmScreen;
import net.whgkswo.tesm.message.MessageHelper;

public class GuiException extends RuntimeException {
    private final TesmScreen motherScreen;
    private static final String LOG_LABEL = "[TESMGui]";

    public GuiException(TesmScreen motherScreen, String message) {
        super(message);
        this.motherScreen = motherScreen;
    }

    public void handle(){
        motherScreen.close();
        MessageHelper.sendMessage(LOG_LABEL + " " + getMessage());
        MessageHelper.sendMessage(LOG_LABEL + " 위치: " + getLocation());
    }

    // 우선순위 1: 상위 클래스와 하위 클래스가 있으면 하위 클래스 우선
    // 우선순위 2: 동일 클래스끼리는 인덱스 낮은 게 우선(가장 최근에 호출된 메서드)
    private String getLocation() {
        StackTraceElement[] trace = getStackTrace();
        StackTraceElement subclassElement = null;
        StackTraceElement baseClassElement = null;

        for (StackTraceElement element : trace) {
            try {
                Class<?> clazz = Class.forName(element.getClassName());

                if (TesmScreen.class.isAssignableFrom(clazz)) {
                    if (!clazz.equals(TesmScreen.class) && subclassElement == null) {
                        // 하위 클래스면서 아직 안 잡았으면 저장
                        subclassElement = element;
                    } else if (clazz.equals(TesmScreen.class) && baseClassElement == null) {
                        // TesmScreen 본인인데 아직 저장 안 했으면 저장
                        baseClassElement = element;
                    }
                }

            } catch (ClassNotFoundException ignored) {}
        }

        StackTraceElement target = subclassElement != null ? subclassElement : baseClassElement;

        if (target != null) {
            String simpleClassName = target.getClassName()
                    .substring(target.getClassName().lastIndexOf('.') + 1);  // 심플 이름만 추출

            return simpleClassName + " - Line " + target.getLineNumber();
        }

        return "클래스 추적 실패";
    }
}
