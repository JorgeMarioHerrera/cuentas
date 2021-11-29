package co.com.bancolombia.usecase.common.util;

import co.com.bancolombia.business.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    public  static String formatVinculationDate(String vinculationDate){
        DateTimeFormatter formatterVinculationDate = DateTimeFormatter.ofPattern(Constants.FORMAT_VINCULATION_DATE);
        LocalDate vinculationDateFormat = LocalDate.parse(vinculationDate,formatterVinculationDate);
        return  vinculationDateFormat.toString();
    }

    public static LocalDate formatOpeningDate(String openingDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ACCOUNT_OPENING_DATE_FORMAT);
        return LocalDate.parse(openingDate, formatter);
    }
    private  DateFormatterUtil(){}
}
