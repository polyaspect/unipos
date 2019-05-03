package unipos.report.components.shared;

/**
 * Created by Thomas on 30.12.2015.
 */
public abstract class DayOfWeekAdapter {

    public static String convertToGerman(String dayOfWeek) {

        switch (dayOfWeek) {
            case "MONDAY":
                return "Montag";
            case "TUESDAY":
                return "Dienstag";
            case "WEDNESDAY":
                return "Mittwoch";
            case "THURSDAY":
                return "Donnerstag";
            case "FRIDAY":
                return "Freitag";
            case "SATURDAY":
                return "Samstag";
            case "SUNDAY":
                return "Sonntag";
            default:
                return "";
        }
    }
}
