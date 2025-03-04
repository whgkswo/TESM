package net.whgkswo.tesm.calendar;

import net.whgkswo.tesm.general.GlobalVariables;

// 불변 객체임! 값을 수정할 때는 항상 새로운 객체 생성
public class InGameTime {
    private int hour;
    private int minute;
    private int second;

    public InGameTime(double hour, double minute) {
        this.hour = (int) hour;
        this.minute = (int) minute;
        this.second = 0;
    }
    public InGameTime(double hour, double minute, double second) {
        this.hour = (int) hour;
        this.minute = (int) minute;
        this.second = (int) second;
    }
    public InGameTime(long tickTime){
        double ticks = (double) tickTime % 24000;

        // 틱을 초로 변환 (24000틱 = 86400초)
        double totalSeconds = (ticks * 86400) / 24000;

        // 초를 시/분/초로 변환
        int adjustedHour = (int)(totalSeconds / 3600);
        totalSeconds -= adjustedHour * 3600;

        int minutes = (int)(totalSeconds / 60);
        totalSeconds -= minutes * 60;

        int seconds = (int)Math.round(totalSeconds);

        // 시간에 6시간 더해서 마인크래프트 기준시간 조정 (0틱 = 6시)
        int hour = (adjustedHour + 6) % 24;

        this.hour = hour;
        this.minute = minutes;
        this.second = seconds;
    }
    public String toString(TimeFormat format, boolean isSecondsIncluded){
        switch (format){
            case COLONAL_24H -> {
                return isSecondsIncluded ? String.format("%02d:%02d:%02d",hour,minute,second) :
                        String.format("%02d:%02d",hour,minute);
            }
            case COLONAL_12H -> {
                String amPm = hour<=12 ? "AM" : "PM";
                // 원칙적으로 정오는 오후 0시가 맞으나 예외적으로 오후 12시로 표기
                int displayHour = (hour == 12) ? 12 : hour % 12;
                return isSecondsIncluded ? String.format("%s %02d:%02d:%02d",amPm,displayHour,minute,second) :
                        String.format("%s %02d:%02d",amPm,displayHour,minute);
            }
            case LINGUAL_24H -> {
                return isSecondsIncluded ? String.format("%02d시 %02d분 %02d초", hour,minute,second) :
                        String.format("%02d시 %02d분", hour,minute);
            }
            case LINGUAL_12H -> {
                String amPm = hour<12 ? "오전" : "오후";
                // 원칙적으로 정오는 오후 0시가 맞으나 예외적으로 오후 12시로 표기
                int displayHour = (hour == 12) ? 12 : hour % 12;
                return isSecondsIncluded ? String.format("%s %02d시 %02d분 %02d초", amPm,displayHour,minute,second) :
                        String.format("%s %02d시 %02d분", amPm,displayHour,minute);
            }
            default -> {
                return null;
            }
        }
    }

    public static InGameTime now(){
         // 현재 틱 시간 가져오기
        double ticks = (double) GlobalVariables.world.getTimeOfDay() % 24000;

        // 틱을 초로 변환 (24000틱 = 86400초)
        double totalSeconds = (ticks * 86400) / 24000;

        // 초를 시/분/초로 변환
        int adjustedHour = (int)(totalSeconds / 3600);
        totalSeconds -= adjustedHour * 3600;

        int minutes = (int)(totalSeconds / 60);
        totalSeconds -= minutes * 60;

        int seconds = (int)Math.round(totalSeconds);

        // 시간에 6시간 더해서 마인크래프트 기준시간 조정 (0틱 = 6시)
        int hour = (adjustedHour + 6) % 24;

        return new InGameTime(hour, minutes, seconds);
    }

    public static InGameTime max(InGameTime timeA, InGameTime timeB){
        return timeA.isAfter(timeB) ? timeA : timeB; // 둘이 동일한 시간이면 timeB 반환
    }

    public static InGameTime min(InGameTime timeA, InGameTime timeB){
        return timeA.isBefore(timeB) ? timeA : timeB; // 둘이 동일한 시간이면 timeB 반환
    }

    public boolean isBefore(InGameTime time) {
        if (this.hour < time.hour) {
            return true;
        } else if (this.hour == time.hour) {
            if (this.minute < time.minute) {
                return true;
            } else if (this.minute == time.minute) {
                return this.second < time.second;
            }
        }
        return false;
    }

    public boolean isAfter(InGameTime time) {
        if (this.hour > time.hour) {
            return true;
        } else if (this.hour == time.hour) {
            if (this.minute > time.minute) {
                return true;
            } else if (this.minute == time.minute) {
                return this.second > time.second;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        InGameTime time = (InGameTime) obj;
        return this.hour == time.hour &&
                this.minute == time.minute &&
                this.second == time.second;
    }

    public boolean isIn(InGameTimePeriod period){
        InGameTime start = period.getStart();
        InGameTime end = period.getEnd();

        if(start.isBefore(end)){ // 정방향 시간: ex) 08시 ~ 15시
            return !(this.isBefore(start) || this.isAfter(end));
        } else if (start.equals(end)) {
            return this.equals(start);
        }else{ // 역방향 시간: ex) 22시 ~ 06시
            return !this.isBefore(start) || !this.isAfter(end);
        }
    }

    public int getTickTime(){
        // 24시간 체계에서 6시를 빼서 마인크래프트 기준 시간으로 변환
        int adjustedHour = (hour - 6 + 24) % 24;

        // 시간, 분, 초를 모두 초 단위로 변환
        double totalSeconds = (adjustedHour * 3600) + (minute * 60) + second;

        // 초를 틱으로 변환 (1일 = 86400초 = 24000틱)
        // 따라서 1초 = 24000/86400 = 5/18 틱
        return (int)Math.round((totalSeconds * 24000) / 86400);
    }

    public InGameTime plusHours(int addition){
        int newHour = customModulo(hour + addition, 24);
        return new InGameTime(newHour, minute, second);
    }
    public InGameTime plusMinutes(int addition){
        int totalMinutes = minute + addition;
        int newHour = customModulo (hour + totalMinutes / 60, 24);
        int newMinute = customModulo(totalMinutes, 60);
        return new InGameTime(newHour, newMinute, second);
    }
    public InGameTime plusSeconds(int addition){
        int totalSeconds = second + addition;
        int totalMinutes = minute + totalSeconds / 60;
        int newHour = customModulo(hour + totalMinutes / 60, 24);
        int newSecond = customModulo(totalSeconds, 60);
        int newMinute = customModulo(totalMinutes, 60);
        return new InGameTime(newHour, newMinute, newSecond);
    }
    // -29시 -> -5시 -> 19시, -48분 -> 12분
    public int customModulo(int num, int size){
        if(num >= 0){
            return num % size;
        }else{
            int rest = num % size;
            return size + rest;
        }
    }

    public enum TimeFormat{
        COLONAL_12H,
        COLONAL_24H,
        LINGUAL_12H,
        LINGUAL_24H,
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int toSeconds(){
        return hour * 3600 + minute * 60 + second;
    }
}
