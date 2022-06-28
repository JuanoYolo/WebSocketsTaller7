package co.edu.escuelaing.websocketsprimer.components;

import co.edu.escuelaing.websocketsprimer.endpoints.TimerEndpoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TimedMessageBroker {

    //Formato de fecha statico
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat("HH:mm:ss");
    private static final Logger logger =
            Logger.getLogger(TimedMessageBroker.class.getName());

    //Metodo, donde usamos Scheduled que viene del framework de springboot, que hace que llame cada 5 segundos llame el metodo broadcast
    @Scheduled(fixedRate = 5000)
    public void broadcast() {
        logger.log(Level.INFO, "broadcastingMessages");
        TimerEndpoint.send("The time is now " + dateFormat.format(new Date()));
    }
}