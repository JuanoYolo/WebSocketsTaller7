package co.edu.escuelaing.websocketsprimer.endpoints;

import java.io.IOException;
import java.util.logging.Level;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/timer")
public class TimerEndpoint {

    //Donde quedan los logs de la plataforma
    private static final Logger logger = Logger.getLogger("ETFEndpoint");

    //Creo una cola statica la cual tiene objetos de tipo sesion, aqui almacenamos sesiones, es una cola concurrente
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    //Metodo que envia mensajes a los clientes
    //Toma la cola y extrae la sesion, estrae el canal y envia el mensaje, y registra el mensaje en el log

    //Se manejan en los loggers estos 3 tipos
    //Informacion
    //Warning
    //Error
    /* Call this method to send a message to all clients */
    public static void send(String msg) {
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                session.getBasicRemote().sendText(msg);
                logger.log(Level.INFO, "Sent: {0}", msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    //Metodo de ciclo de vida del websocket, registre la sesion en la cola cuando se abra la conexion
    @OnOpen
    public void openConnection(Session session) {
        /* Register this connection in the queue */
        queue.add(session);
        logger.log(Level.INFO, "Connection opened.");

        //Apenas me conecto y envio mensaje de conexion satisfactoria
        try {
            session.getBasicRemote().sendText("Connection established.");
        } catch (IOException ex) {
            Logger.getLogger(TimerEndpoint.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    //Cuando se cierre la sesion, quito la sesion de la cola
    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    //Si hay error, quito la sesion de la cola y registro el error
    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }
}
