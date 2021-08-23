package utils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateTimeFormat {
    
    private final List<DateTimeFormatter> dateFormatterList = new ArrayList<>();
    
    private final List<DateTimeFormatter> timeFormatterList = new ArrayList<>();
    
    public static DateTimeFormat generate() {
        DateTimeFormat dateTimeFormat = new DateTimeFormat();
        dateTimeFormat.init();
        return dateTimeFormat;
    }
    
    private void init() {
        initialiseDateFormats();
        initialiseTimeFormats();
    }

    private void initialiseDateFormats() {
        dateFormatterList.clear();
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        dateFormatterList.add(DateTimeFormatter.ofPattern("dd/M/yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy/M/dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd-M-yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy-M-dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd.M.yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy.M.dd"));

        dateFormatterList.add(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy/MMMM/dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy-MMMM-dd"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("dd.MMMM.yyyy"));
        dateFormatterList.add(DateTimeFormatter.ofPattern("yyyy.MMMM.dd"));
    }

    private void initialiseTimeFormats() {
        timeFormatterList.clear();
        timeFormatterList.add(DateTimeFormatter.ofPattern("HH:mm"));
        timeFormatterList.add(DateTimeFormatter.ofPattern("hh:mm a"));

        timeFormatterList.add(DateTimeFormatter.ofPattern("HH:mm:ss"));
        timeFormatterList.add(DateTimeFormatter.ofPattern("hh:mm:ss a"));

        timeFormatterList.add(DateTimeFormatter.ofPattern("HHmm"));
        timeFormatterList.add(DateTimeFormatter.ofPattern("hhmm a"));
    }

    public List<DateTimeFormatter> getDateFormatterList() {
        return dateFormatterList;
    }

    public List<DateTimeFormatter> getTimeFormatterList() {
        return timeFormatterList;
    }
}
