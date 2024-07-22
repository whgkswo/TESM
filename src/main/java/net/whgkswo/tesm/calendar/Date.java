package net.whgkswo.tesm.calendar;

public class Date {
    private int year;
    private Month month;
    private int day;
    private DayOfTheWeek dayOfTheWeek;

    public Date(int year, Month month, int day, DayOfTheWeek dayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public void addDate(){
        day++;
        addDayOfTheWeek();
        if(day > month.getDaysInMonth()){
            day = 1;
            month = Month.getNextMonth(month);
            if(month == Month.MORNING_STAR){
                year++;
            }
        }
    }
    public void addDayOfTheWeek(){
        switch (dayOfTheWeek){
            case MORNDAS -> {
                dayOfTheWeek = DayOfTheWeek.TIRDAS;
            }
            case TIRDAS -> {
                dayOfTheWeek = DayOfTheWeek.MIDDAS;
            }
            case MIDDAS -> {
                dayOfTheWeek = DayOfTheWeek.TURDAS;
            }
            case TURDAS -> {
                dayOfTheWeek = DayOfTheWeek.FREDAS;
            }
            case FREDAS -> {
                dayOfTheWeek = DayOfTheWeek.LOREDAS;
            }
            case LOREDAS -> {
                dayOfTheWeek = DayOfTheWeek.SUNDAS;
            }
            case SUNDAS -> {
                dayOfTheWeek = DayOfTheWeek.MORNDAS;
            }
        }
    }
    public String toString(){
        return String.format("3E %d, %d월 %d일 (%s)", year,month.getNumber(),day, dayOfTheWeek.name);
    }
    public enum DayOfTheWeek{
        MORNDAS("월"),
        TIRDAS("화"),
        MIDDAS("수"),
        TURDAS("목"),
        FREDAS("금"),
        LOREDAS("토"),
        SUNDAS("일")
        ;
        private String name;

        DayOfTheWeek(String name) {
            this.name = name;
        }
    }
}
