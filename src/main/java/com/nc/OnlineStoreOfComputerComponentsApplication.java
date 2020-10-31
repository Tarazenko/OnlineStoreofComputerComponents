package com.nc;

import com.nc.service.Impl.CurrencySingleton;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OnlineStoreOfComputerComponentsApplication {
    private final static Logger logger = Logger.getLogger(OnlineStoreOfComputerComponentsApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(OnlineStoreOfComputerComponentsApplication.class, args);
    }

    @PostConstruct
    private void init() {
        logger.info("Taking currency with API nat. Bank of Belarus.");
        CurrencySingleton.updateCurrencyFromNBRBAPI();
        logger.info("Starting a timer to update the currency.");
        CurrencySingleton.startSchedule();
    }
}
