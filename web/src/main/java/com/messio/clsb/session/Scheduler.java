package com.messio.clsb.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messio.clsb.Transfer;
import com.messio.clsb.adapter.LocalTimeAdapter;
import com.messio.clsb.entity.*;
import com.messio.clsb.entity.Currency;
import com.messio.clsb.event.BankEvent;
import com.messio.clsb.event.BaseEvent;
import com.messio.clsb.event.CurrencyEvent;
import com.messio.clsb.model.BankModel;
import com.messio.clsb.util.script.Environment;
import com.messio.clsb.util.script.Parser;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jpc on 10/3/16.
 */
@Singleton(name = "clsb/scheduler")
@LocalBean
@Startup
@Path("/")
@Produces({"application/json"})
@ServerEndpoint("/scheduler")
public class Scheduler extends Environment {
    public static final Logger LOGGER = Logger.getLogger(Scheduler.class.getCanonicalName());
    @Inject
    private ClsbFacade facade;
    @Inject
    private Event<BaseEvent> emitter;
    @Resource(lookup = "java:/mail/GmailMessio")
    private javax.mail.Session session;

    private List<BaseEvent> events;
    private int index;
    private LocalTime now;
    private Set<Session> sessions = new HashSet<>();


    @PostConstruct
    public void init() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        if (facade.findBank() == null){
            try (final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/initial.json")) {
                final BankModel bankModel = objectMapper.readValue(is, BankModel.class);
                facade.create(bankModel.getBank());
                for (Currency currency: bankModel.getCurrencies()){
                    currency.setBank(bankModel.getBank());
                    facade.create(currency);
                }
                for (Account account: bankModel.getAccounts()){
                    account.setBank(bankModel.getBank());
                    facade.create(account);
                }
            } catch(IOException e){
                LOGGER.severe(e.getMessage());
            }
        }

        this.events = new ArrayList<>();
        events.add(new BaseEvent(LocalTime.MIN, "init"));
        events.add(new BaseEvent(LocalTime.MAX, "done"));
        final Bank bank = facade.findBank();
        events.add(new BankEvent(bank.getOpening(), "opening", bank));
        events.add(new BankEvent(bank.getSettlementCompletionTarget(), "sct", bank));
        events.add(new BankEvent(bank.getClosing(), "closing", bank));
        for (final Currency currency: facade.findCurrencies(bank)){
            events.add(new CurrencyEvent(currency.getOpening(), "opening", currency));
            events.add(new CurrencyEvent(currency.getFundingCompletionTarget(), "fct", currency));
            events.add(new CurrencyEvent(currency.getClose(), "close", currency));
            events.add(new CurrencyEvent(currency.getClosing(), "closing", currency));
        }
        events.sort((e1, e2) -> e1.getWhen().equals(e2.getWhen()) ? 0 : (e1.getWhen().isAfter(e2.getWhen()) ? 1 : -1));

        for (final BaseEvent event: events){
            LOGGER.info(String.format("Event: %s", event));
        }
    }

    private void fireEvent(BaseEvent event){
        emitter.fire(event);
        this.now = event.getWhen();
    }

    @GET
    @Path("/bank")
    public Bank bank(){
        return facade.findBank();
    }

    @GET
    @Path("/command/{cmd}")
    public LocalTime command(@PathParam("cmd") String cmd){
        try{
            eval(cmd);
        } catch (ParseException e){
            LOGGER.severe(e.getMessage());
        }
        return now;
    }

    @Override
    public Object call(String function, List<Object> arguments) {
        LOGGER.info(String.format("%s(%s)", function, arguments.stream().map(Parser::toString).collect(Collectors.joining(","))));
        switch(function){
            case "reset":
                this.index = 0;
                return call("step", Collections.emptyList());
            case "step":
                if (index < events.size()){
                    fireEvent(events.get(this.index));
                    this.index++;
                }
                break;
            case "all":
                events.forEach(this::fireEvent);
                break;
            case "mail-test":
                try {
                    session.setDebug(true);
                    final MimeMessage message = new MimeMessage(session);
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress("jpcuvelliez@gmail.com"));
                    message.setSubject("Test Email");
                    message.setText("Some test text");
                    Transport.send(message);
                } catch (MessagingException e){
                    LOGGER.severe(e.getMessage());
                }
                break;
        }
        sendMessage(now, "");
        return null;
    }

    @OnOpen
    public void open(Session session){
        LOGGER.info(String.format("WS session opened: %s", session.getId()));
        sessions.add(session);
    }

    @OnClose
    public void close(Session session){
        LOGGER.info(String.format("WS session closed: %s", session.getId()));
        sessions.remove(session);
    }

    @OnError
    public void error(Throwable t){
        LOGGER.severe(String.format("WS error: %s", t.getMessage()));
    }

    @OnMessage
    public void handleMessage(String message, Session session){
        LOGGER.info(String.format("WS message received from session %s: %s", session.getId(), message));
    }

    @Asynchronous
    public void sendMessage(LocalTime when, String message){
        final JsonProvider provider = JsonProvider.provider();
        final JsonObject o = provider.createObjectBuilder()
                .add("localTime", LocalTimeAdapter.CONVERTER.convertToDatabaseColumn(when))
                .add("message", message)
                .build();
        for (Session session: sessions){
            if (session.isOpen()) try {
                session.getBasicRemote().sendText(o.toString());
            } catch (IOException e){
                LOGGER.severe(e.getMessage());
            }
        }
    }




}
