package net.whgkswo.tesm.data;


import net.whgkswo.tesm.general.GlobalVariables;

public class ServerResourceHelper extends ResourceHelper{
    private static final ServerResourceHelper INSTANCE = new ServerResourceHelper();

    // 함수형 인터페이스 구현하여 서버 리소스 매니저 전달
    private ServerResourceHelper() {
        super(() -> GlobalVariables.world.getServer().getResourceManager());
    }

    // 본래 유틸리티 클래스였으나 상속 기능을 사용하게 되면서 정적 메서드가 사용 불가해짐. 편리하게 사용하기 위해 싱글톤 객체를 추가
    public static ServerResourceHelper getInstance() {
        return INSTANCE;
    }
}
