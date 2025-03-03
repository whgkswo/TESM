package net.whgkswo.tesm.calendar;

// 불변 객체임! 값을 수정할 때는 항상 새로운 객체 생성
public class InGameDateTimePeriod {
    private InGameDateTime start;
    private InGameDateTime end;

    public InGameDateTimePeriod(InGameDateTime start, InGameDateTime end){
        // TODO: InGameDateTime 내부에 max, min 메서드 구현하여 적용
        this.start = start;
        this.end = end;
    }

    public InGameDateTime getStart(){
        return start;
    }
    public InGameDateTime getEnd(){
        return end;
    }
}
