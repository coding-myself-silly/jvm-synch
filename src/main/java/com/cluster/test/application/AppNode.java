package com.cluster.test.application;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import org.apache.log4j.Logger;

public class AppNode implements Callable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(AppNode.class);

    private static final String MAP_NAME = "synchPoint";

    private static final String MAP_KEY = "initialized";

    private String name = null;

    private long sleepTime;

    /**
     * 
     * @param name
     * @param sleepTime
     */
    public AppNode(String name, long sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    /**
     * @return the name
     */
    public String call() {
        System.setProperty("hazelcast.logging.type", "log4j");
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        IMap<String, Boolean> synchPoint = instance.getMap(MAP_NAME);
        Boolean existing = synchPoint.put(MAP_KEY, Boolean.TRUE);
        if (existing == null) {
            System.out.println("We are started!");
            LOGGER.info("We are started!");
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ie) {
            LOGGER.error("Got interrupted", ie);
        }
        return name;
    }

}
