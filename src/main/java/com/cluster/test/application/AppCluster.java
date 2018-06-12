package com.cluster.test.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.apache.log4j.Logger;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class AppCluster {

    private static final Logger LOGGER = Logger.getLogger(AppCluster.class);

    private static final String NODE_PREFIX = "NODE_";

    /**
     * 
     * @param numberOfNodes
     *            the number of nodes to execute
     */
    public void createCluster(int numberOfNodes) {
        List<AppNode> tasks = new ArrayList<AppNode>();
        for (int i = 1; i <= numberOfNodes; i++) {
            AppNode node = new AppNode(NODE_PREFIX + i, (numberOfNodes - i) * 10000);
            tasks.add(node);
        }
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        IExecutorService executor = instance.getExecutorService("executor");
        try {
            // returnValues captured in case we need them in the next business case
            List<Future<String>> returnValues = executor.invokeAll(tasks);
        } catch (InterruptedException ie) {
            LOGGER.error("InterruptedException invoking tasks", ie);
        } catch (RejectedExecutionException ree) {
            LOGGER.error(" RejectedExecutionException invoking tasks", ree);
        } catch (NullPointerException npe) {
            LOGGER.error(" NullPointerException invoking tasks", npe);
        }
        instance.shutdown();
    }

}
