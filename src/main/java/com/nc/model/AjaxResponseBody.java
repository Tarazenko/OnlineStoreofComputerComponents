package com.nc.model;

import java.util.List;
import java.util.Set;

public class AjaxResponseBody {

    private String msg;
    private Set<Hardware> result;
    private List<Double> currency;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Set<Hardware> getResult() {
        return result;
    }

    public void setResult(Set<Hardware> result) {
        this.result = result;
    }

    public List<Double> getCurrency() {
        return currency;
    }

    public void setCurrency(List<Double> currency) {
        this.currency = currency;
    }
}
