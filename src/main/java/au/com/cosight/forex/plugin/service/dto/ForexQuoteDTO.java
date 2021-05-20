package au.com.cosight.forex.plugin.service.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class ForexQuoteDTO {
    private String currencyCode;
    private Double rate;
    private Date dateTime;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ForexQuoteDTO{" +
                "currencyCode='" + currencyCode + '\'' +
                ", rate=" + rate +
                ", dateTime=" + dateTime +
                '}';
    }
}
