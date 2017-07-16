package org.clickcounter.hz.server;

import com.hazelcast.core.*;
import com.hazelcast.config.*;

/**
 * Created by joaopires on 15/07/17.
 */
public class HazelCastServer {
    public static void main(String[] args) {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
    }
}
