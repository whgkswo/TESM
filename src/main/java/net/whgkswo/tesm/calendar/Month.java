package net.whgkswo.tesm.calendar;

public enum Month {
    MORNING_STAR(1,"Morning Star",31),
    SUNS_DAWN(2,"Sun's Dawn",28),
    FIRST_SEED(3,"First Seed",31),
    RAINS_HAND(4,"Rain's Hand",30),
    SECOND_SEED(5,"Second Seed",31),
    MID_YEAR(6,"Mid Year",30),
    SUNS_HEIGHT(7,"Sun's Height",31),
    LAST_SEED(8,"Last Seed",31),
    HEARTHFIRE(9,"Hearthfire",30),
    FROST_FALL(10,"Frost Fall",31),
    SUNS_DUSK(11,"Sun's Dusk",30),
    EVENING_STAR(12,"Evening Star",31)
    ;
    private int number;
    private String name;
    private int daysInMonth;

    Month(int number, String name, int daysInMonth) {
        this.number = number;
        this.name = name;
        this.daysInMonth = daysInMonth;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getDaysInMonth() {
        return daysInMonth;
    }
    public static Month getNextMonth(Month month){
        for(Month nextMonth : Month.values()){
            if(month.number == 12){
                return MORNING_STAR;
            }
            if(nextMonth.getNumber() == month.number + 1){
                return nextMonth;
            }
        }
        return null;
    }
}
