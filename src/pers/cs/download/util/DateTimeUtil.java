package pers.cs.download.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern(DATE_PATTERN);

   
    public static String format(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

  
    public static LocalDateTime parse(String dateString) {
        try {
            return LocalDateTime.parse(DATE_PATTERN, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
        	e.printStackTrace();
            return null;
        }
    }
}