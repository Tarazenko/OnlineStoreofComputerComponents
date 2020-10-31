package com.nc.service.Impl;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurrencySingleton {
    private static double result;
    private final static Logger LOGGER = Logger.getLogger(CurrencySingleton.class);


    public static double getDollarCurrency() {
        LOGGER.info("Getting the currency value(" + result + ").");
        return result;
    }

    public static void startSchedule() {
        LOGGER.info("Running a timer for currency updates.");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(CurrencySingleton::updateCurrencyFromNBRBAPI, 12, 12, TimeUnit.HOURS);
    }

    public static void updateCurrencyFromNBRBAPI() {
        LOGGER.info("Taking currency value from api nat. Bank of Belarus.");
        RestTemplate restTemplate = new RestTemplate();
        LinkedHashMap currency = restTemplate.getForObject(
                "http://www.nbrb.by/api/exrates/rates/840?parammode=1", LinkedHashMap.class);
        result = Double.parseDouble(currency.get("Cur_OfficialRate").toString());
    }
}

