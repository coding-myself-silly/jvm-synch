package com.cluster.test.application;

import org.junit.Test;

public class AppClusterTest {

    @Test
    public void testCreateCluster() {
        System.setProperty("hazelcast.logging.type", "log4j");
        AppCluster c = new AppCluster();
        c.createCluster(10);
    }

}
