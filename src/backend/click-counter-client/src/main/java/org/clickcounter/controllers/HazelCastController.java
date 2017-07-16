package org.clickcounter.controllers;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import org.clickcounter.server.ClickCounterWSServlet;
import org.clickcounter.utils.Constants;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created by joaopires on 15/07/17.
 */
public class HazelCastController {
    //FIXME: A Singleton doesn't guarantee scalability; Could be a Stateless EJB or something like that
    private static HazelCastController instance;

    private HazelcastInstance hzClient;

    private Queue<ClickCounterWSServlet> clientsQueue;
    //private Long counter = 0L;
    private static final Logger log = Logger.getLogger(HazelCastController.class.getSimpleName());


    private HazelCastController() {
        hzClient = HazelcastClient.newHazelcastClient();
        clientsQueue = new ConcurrentLinkedQueue<ClickCounterWSServlet>();
    }

    public static synchronized HazelCastController getInstance() {
        if(instance == null) {
            instance = new HazelCastController();
        }
        return instance;
    }

    public void putClientInQueue(ClickCounterWSServlet wsClient) {
        //hzClient.getQueue(Constants.QUEUE_NAME).offer(wsClient);
        clientsQueue.offer(wsClient);
    }

    public void removeClientFromQueue(ClickCounterWSServlet wsClient) {
        //hzClient.getQueue(Constants.QUEUE_NAME).remove(wsClient);
        clientsQueue.remove(wsClient);
    }

    /*public void incCounter() {
        counter++;
    }

    public void decCounter() {
        counter--;
    }*/

    public IAtomicLong getAtomicCounter() {
        return hzClient.getAtomicLong(Constants.COUNTER_NAME);
    }

    public long getCounter() {
        return getAtomicCounter().get();
    }

    /*public void broadcastCounter() {
        long c = getCounter();
        for(ClickCounterWSServlet wsClient : clientsQueue) {
            try {
                wsClient.getSession().getRemote().sendString(String.valueOf(c));
            } catch (IOException e) {
                e.printStackTrace();
                log.severe("Error broadcasting counter to: " + wsClient.getSessionId());
            }
        }
    }*/

    public void broadcastCounter(String opt) {
        long counter = 0L;
        if(Constants.INCREMENT_OPT.equals(opt)) {
            counter = getAtomicCounter().incrementAndGet();
        } else if(Constants.DECREMENT_OPT.equals(opt)) {
            counter = getAtomicCounter().decrementAndGet();
        } else {
            //just ignore it
            return ;
        }
        for(ClickCounterWSServlet wsClient : clientsQueue) {
            try {
                wsClient.getSession().getRemote().sendString(String.valueOf(counter));
            } catch (IOException e) {
                e.printStackTrace();
                log.severe("Error broadcasting counter to: " + wsClient.getSessionId());
            }
        }
    }
}
