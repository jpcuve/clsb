package com.messio.clsb.session;

import com.messio.clsb.Transfer;
import com.messio.clsb.entity.Instruction;
import com.messio.clsb.entity.PayIn;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 22-09-16.
 */
@Singleton(name = "clsb/pay-in-manager")
@LocalBean
public class PayInManager {
    public static final Logger LOGGER = Logger.getLogger(PayInManager.class.getCanonicalName());
    private List<PayIn> payIns;

    public PayInManager() {
    }

    @Inject
    public PayInManager(List<Instruction> instructions) {
        this.payIns = instructions.stream()
                .filter(i -> i instanceof PayIn)
                .map(i -> (PayIn) i)
                .collect(Collectors.toList());
    }

    public List<Transfer> bookPayIns(final LocalTime now, final String iso){
        LOGGER.info(String.format("Booking pay-ins for %s", iso));
        return payIns.stream()
                .filter(pi -> pi.getTransferred() == null && pi.getWhen().isBefore(now) && pi.getAmount().containsKey(iso))
                .map(Transfer::new)
                .collect(Collectors.toList());
    }
}
