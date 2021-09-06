package au.com.cosight.xero.plugin.mapper;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.sql.Date;

public class BaseMapper {
    public static java.util.Date getDate(LocalDate localDate) {
//        LocalDate instant = bankTransaction.getDateAsDate();
        java.util.Date newDate = null;
        try {
            Date date = DateTimeUtils.toSqlDate(localDate);
            newDate = new java.util.Date(date.getTime());
        } catch (Exception e) {
        }
        return newDate;

    }

    public static java.util.Date getDate(OffsetDateTime offset) {
        if(offset!=null){
            return DateTimeUtils.toDate(offset.toInstant());
        }
        else{
            return null;
        }
    }


}
