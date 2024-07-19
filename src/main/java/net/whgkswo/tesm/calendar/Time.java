package net.whgkswo.tesm.calendar;

import net.whgkswo.tesm.general.GlobalVariables;

public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(double hour, double minute, double second) {
        this.hour = (int) hour;
        this.minute = (int) minute;
        this.second = (int) second;
    }
    public String toString(TimeFormat format, boolean secondIncluded){
        switch (format){
            case COLONAL_24H -> {
                return secondIncluded ? String.format("%d:%d:%d",hour,minute,second) :
                        String.format("%d:%d",hour,minute);
            }
            case COLONAL_12H -> {
                String amPm = hour<=12 ? "AM" : "PM";
                // 원칙적으로 정오는 오후 0시가 맞으나 예외적으로 오후 12시로 표기
                int displayHour = (hour == 12) ? 12 : hour % 12;
                return secondIncluded ? String.format("%s %d:%d:%d",amPm,displayHour,minute,second) :
                        String.format("%s %d:%d",amPm,displayHour,minute);
            }
            case LINGUAL_24H -> {
                return secondIncluded ? String.format("%d시 %d분 %d초", hour,minute,second) :
                        String.format("%d시 %d분", hour,minute);
            }
            case LINGUAL_12H -> {
                String amPm = hour<12 ? "오전" : "오후";
                // 원칙적으로 정오는 오후 0시가 맞으나 예외적으로 오후 12시로 표기
                int displayHour = (hour == 12) ? 12 : hour % 12;
                return secondIncluded ? String.format("%s %d시 %d분 %d초", amPm,displayHour,minute,second) :
                        String.format("%s %d시 %d분", amPm,displayHour,minute);
            }
            default -> {
                return null;
            }
        }
    }

    public static Time getTime(){
        double time = (double) GlobalVariables.world.getTimeOfDay() % 24000;
        double hour = time/1000;
        time -= (int) hour * 1000;
        double minute = time / (1000.0/60);
        time -= (int) minute * (1000.0/60);
        double second = time / (1000.0/3600);
        hour = (hour+6) % 24;
        return new Time(hour,minute,second);
    }
    public enum TimeFormat{
        COLONAL_24H,
        COLONAL_12H,
        LINGUAL_24H,
        LINGUAL_12H
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
}
