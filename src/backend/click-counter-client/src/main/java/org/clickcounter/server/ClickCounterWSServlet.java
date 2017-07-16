package org.clickcounter.server;

import org.clickcounter.controllers.HazelCastController;
import org.clickcounter.utils.Constants;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by joaopires on 14/07/17.
 */
@WebSocket
public class ClickCounterWSServlet extends WebSocketServlet implements Serializable {


    private String sessionId;
    private Session session;
    private static final Logger log = Logger.getLogger(ClickCounterWSServlet.class.getSimpleName());

    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info("Client connected: " + session.getRemoteAddress().toString());
        this.sessionId = session.getRemoteAddress().toString();
        this.session = session;
        HazelCastController.getInstance().putClientInQueue(this);

        try {
            session.getRemote().sendString(String.valueOf(HazelCastController.getInstance().getCounter()));
        } catch (IOException e) {
            log.severe("Error sending message to browser when connected");
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        /*if(Constants.INCREMENT_OPT.equals(message)) {
            HazelCastController.getInstance().incCounter();
        } else if(Constants.DECREMENT_OPT.equals(message)) {
            HazelCastController.getInstance().decCounter();
        } else {
            //just ignore it
            return;
        }*/
        HazelCastController.getInstance().broadcastCounter(message);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        this.sessionId = session.getRemoteAddress().toString();
        HazelCastController.getInstance().removeClientFromQueue(this);
        log.info("Coneection closed between us and the client: " + sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClickCounterWSServlet that = (ClickCounterWSServlet) o;

        return sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    public void configure(WebSocketServletFactory factory) {
        //FIXME: What is the right idle timeout time?
        factory.getPolicy().setIdleTimeout(10000000);
        factory.register(ClickCounterWSServlet.class);
    }
}
