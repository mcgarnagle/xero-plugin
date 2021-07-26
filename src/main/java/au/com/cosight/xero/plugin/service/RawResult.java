package au.com.cosight.xero.plugin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawResult {

    private Map<String,Double> quotes = new HashMap<>();
    private boolean success;
    private long timestamp;
    private Map<String,Object> error;

    public Map<String, Double> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, Double> quotes) {
        this.quotes = quotes;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }
}
