package com.cluster.test.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class AppNodeTest {

    private static final String NODE_PREFIX_FIRST_RUN = "Node_First_";

    private static final String NODE_PREFIX_SECOND_RUN = "Node_Second_";

    @Test
    public void fullRequirementTest() throws Exception {
        System.setProperty("hazelcast.logging.type", "log4j");
        int numberOfNodes = 6;
        List<AppNode> tasks = new ArrayList<AppNode>();
        AppNode restarter = new AppNode("Restart Node", 999L);
        AppNode neverRun = new AppNode("Never Run", 999L);
        tasks.add(restarter);
        for (int i = 1; i <= numberOfNodes; i++) {
            AppNode node = new AppNode(NODE_PREFIX_FIRST_RUN + i, (numberOfNodes - i) * 1000);
            tasks.add(node);
        }
        HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        IExecutorService executor = instance.getExecutorService("executor");
        List<Future<String>> returnValues = executor.invokeAll(tasks);
        Future<String> result = executor.submit(restarter);
        tasks.clear();
        returnValues = executor.invokeAll(tasks);
        numberOfNodes = 2;
        for (int i = 1; i <= numberOfNodes; i++) {
            AppNode node = new AppNode(NODE_PREFIX_SECOND_RUN + i, (numberOfNodes - i) * 1000);
            tasks.add(node);
        }
        Thread.sleep(30000);
        restarter = new AppNode("Start after sleep", 600L);
        result = executor.submit(restarter);
        instance.shutdown();
    }

}
