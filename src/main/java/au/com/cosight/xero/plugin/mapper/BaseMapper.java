package au.com.cosight.xero.plugin.mapper;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.sql.Date;

public class BaseMapper {
    public static java.util.Date getDate(LocalDate localDate) {
//        LocalDate instant = bankTransaction.getDateAsDate();
        Date date = DateTimeUtils.toSqlDate(localDate);
        java.util.Date newDate = new java.util.Date(date.getTime());
        return newDate;

    }

    public static java.util.Date getDate(OffsetDateTime offset) {
        return DateTimeUtils.toDate(offset.toInstant());
    }
}
