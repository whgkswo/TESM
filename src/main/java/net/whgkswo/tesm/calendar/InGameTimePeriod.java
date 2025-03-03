package net.whgkswo.tesm.calendar;

// 불변 객체임! 값을 수정할 때는 항상 새로운 객체 생성
public class InGameTimePeriod {
    private final InGameTime start;
    private final InGameTime end;

    public InGameTimePeriod(InGameTime start, InGameTime end){
        // max, min을 사용해서는 안 됨: 저녁 22시 ~ 익일 06시 같은 시간을 구현하려면
        this.start = start;
        this.end = end;
    }
    public InGameTimePeriod(int startH, int startM, int endH, int endM){
        start = new InGameTime(startH, startM);
        end = new InGameTime(endH, endM);
    }
    public InGameTimePeriod(int startH, int startM, int startS, int endH, int endM, int endS){
        start = new InGameTime(startH, startM, startS);
        end = new InGameTime(endH, endM, endS);
    }

    public InGameTime getStart(){
        return start;
    }
    public InGameTime getEnd(){
        return end;
    }
    public int getLength(){
        int startSeconds = start.toSeconds();
        int endSeconds = end.toSeconds();

        if (start.isBefore(end)) { // 정방향 (같은 날 내에서 시작과 종료)
            return endSeconds - startSeconds;
        } else if (start.equals(end)) {
            return 0;
        } else { // 역방향 (자정을 넘어감) 자정: 86400초
            return 86400 - startSeconds + endSeconds;
        }
    }
    public int getTickLength(){
        if(start.isBefore(end)){ // 정방향
            return end.getTickTime() - start.getTickTime();
        } else if (start.equals(end)) {
            return 0;
        }else{ // 역방향: 자정: 18000틱
            return 18000 - start.getTickTime() + end.getTickTime();
        }
    }
    public float getProgressRatio(InGameTime time){
        if(!time.isIn(this)) return 0;

        int startSeconds = start.toSeconds();
        int timeSeconds = time.toSeconds();
        int length = this.getLength();

        if(start.isBefore(end)){ // 정방향
            return (float) (timeSeconds - startSeconds) / length;
        } else if (start.equals(end)) {
            return 1.0f;
        }else { // 역방향
            int elapsedSeconds;
            if (timeSeconds >= startSeconds) { // 자정을 넘기지 않음
                elapsedSeconds = timeSeconds - startSeconds;
            } else { // 자정 넘김
                elapsedSeconds = (86400 - startSeconds) + timeSeconds;
            }
            return (float) elapsedSeconds / length;
        }
    }
}
