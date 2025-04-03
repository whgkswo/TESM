package net.whgkswo.tesm.networking.payload.id;

/**
 * 기본적으로 NullRes를 발생시키는 요청을 구분하기 위해 사용
 * NullRes를 받는 리시버가 취해야 할 동작을 결정
 */
public enum RequestReason {
    CENTER_RAYCAST_BLOCK    // 레이캐스팅 된 블록이 상호작용 불가능
    ;
}
