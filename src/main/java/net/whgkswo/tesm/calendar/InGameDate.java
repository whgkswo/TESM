package net.whgkswo.tesm.calendar;

// 불변 객체임! 값을 수정할 때는 항상 새로운 객체 생성
public class InGameDate {
    private int year;
    private Month month;
    private int day;
    private DayOfTheWeek dayOfTheWeek;

    public InGameDate(int year, Month month, int day, DayOfTheWeek dayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    // TODO: 새로운 객체 반환하도록 수정 필요, plusDays() 등의 메서드 구현 고려
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
