package com.device.api.stat;

import com.codahale.metrics.MetricRegistry;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by  on 4/27/16.
 */
public class Metrics {
    private static MetricRegistry INSTANCE;
    private static Object lock = new Object();

    public static MetricRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new MetricRegistry();
                    final MetricsLogger reporter = MetricsLogger.forRegistry(INSTANCE)
                            .outputTo(LoggerFactory.getLogger(Metrics.class))
                            .build();
                    reporter.start(10, TimeUnit.SECONDS);
                }
            }
        }
        return INSTANCE;
    }

}
