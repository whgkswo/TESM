package net.whgkswo.tesm.calendar;

// 불변 객체임! 값을 수정할 때는 항상 새로운 객체 생성
public class InGameDateTime {
    private InGameDate inGameDate;
    private InGameTime inGameTime;

    public InGameDate getDate() {
        return inGameDate;
    }

    public InGameTime getTime() {
        return inGameTime;
    }
}
