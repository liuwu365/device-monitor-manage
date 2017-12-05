package com.device.api.stat;

import com.codahale.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.text.DecimalFormat;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * copied from Slf4jReporter, to add count delta
 */

/**
 * A reporter class for logging metrics values to a SLF4J {@link Logger} periodically, similar to
 * {@link ConsoleReporter} or {@link CsvReporter}, but using the SLF4J framework instead. It also
 * supports specifying a {@link Marker} instance that can be used by custom appenders and filters
 * for the bound logging toolkit to further process metrics reports.
 */
public class MetricsLogger extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link MetricsLogger}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link MetricsLogger}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    public enum LoggingLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    /**
     * A builder for {@link CsvReporter} instances. Defaults to logging to {@code metrics}, not
     * using a marker, converting rates to events/second, converting durations to milliseconds, and
     * not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private Logger logger;
        private LoggingLevel loggingLevel;
        private Marker marker;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.logger = LoggerFactory.getLogger(Metrics.class);
            this.marker = null;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
            this.loggingLevel = LoggingLevel.INFO;
        }

        /**
         * Log metrics to the given logger.
         *
         * @param logger an SLF4J {@link Logger}
         * @return {@code this}
         */
        public Builder outputTo(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Mark all logged metrics with the given marker.
         *
         * @param marker an SLF4J {@link Marker}
         * @return {@code this}
         */
        public Builder markWith(Marker marker) {
            this.marker = marker;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Use Logging Level when reporting.
         *
         * @param loggingLevel a (@link Slf4jReporter.LoggingLevel}
         * @return {@code this}
         */
        public Builder withLoggingLevel(LoggingLevel loggingLevel) {
            this.loggingLevel = loggingLevel;
            return this;
        }

        /**
         * Builds a {@link MetricsLogger} with the given properties.
         *
         * @return a {@link MetricsLogger}
         */
        public MetricsLogger build() {
            LoggerProxy loggerProxy;
            switch (loggingLevel) {
                case TRACE:
                    loggerProxy = new TraceLoggerProxy(logger);
                    break;
                case INFO:
                    loggerProxy = new InfoLoggerProxy(logger);
                    break;
                case WARN:
                    loggerProxy = new WarnLoggerProxy(logger);
                    break;
                case ERROR:
                    loggerProxy = new ErrorLoggerProxy(logger);
                    break;
                default:
                case DEBUG:
                    loggerProxy = new DebugLoggerProxy(logger);
                    break;
            }
            return new MetricsLogger(registry, loggerProxy, marker, rateUnit, durationUnit, filter);
        }
    }

    private final LoggerProxy loggerProxy;
    private final Marker marker;
    private final ConcurrentHashMap<String, Long> delta;

    private MetricsLogger(MetricRegistry registry, LoggerProxy loggerProxy, Marker marker, TimeUnit rateUnit,
                          TimeUnit durationUnit, MetricFilter filter) {
        super(registry, "logger-reporter", filter, rateUnit, durationUnit);
        this.loggerProxy = loggerProxy;
        this.marker = marker;
        this.delta = new ConcurrentHashMap<String, Long>();
    }

    private long getAndSetLastCountFor(String name, long newCount) {
        Long count = delta.put(name, newCount);
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        for (Entry<String, Gauge> entry : gauges.entrySet()) {
            logGauge(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Counter> entry : counters.entrySet()) {
            logCounter(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Histogram> entry : histograms.entrySet()) {
            logHistogram(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Meter> entry : meters.entrySet()) {
            logMeter(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Timer> entry : timers.entrySet()) {
            logTimer(entry.getKey(), entry.getValue());
        }
    }

    private void logTimer(String name, Timer timer) {
        final Snapshot snapshot = timer.getSnapshot();
        long lastCount = getAndSetLastCountFor(name + ".timer", timer.getCount());
        // do not print delta == 0
        if (timer.getCount() - lastCount > 0) {
            loggerProxy.log(marker,
                    "type=TIMER, name={}, count={}, delta={}, min={}, max={}, mean={}, stddev={}, median={}, "
                            + "p75={}, p95={}, p98={}, p99={}, p999={}, mean_rate={}, m1={}, m5={}, "
                            + "m15={}, rate_unit={}, duration_unit={}", name, timer.getCount(), timer.getCount()
                            - lastCount, convertDurationStr(snapshot.getMin()), convertDurationStr(snapshot.getMax()),
                    convertDurationStr(snapshot.getMean()), convertDurationStr(snapshot.getStdDev()),
                    convertDurationStr(snapshot.getMedian()), convertDurationStr(snapshot.get75thPercentile()),
                    convertDurationStr(snapshot.get95thPercentile()), convertDurationStr(snapshot.get98thPercentile()),
                    convertDurationStr(snapshot.get99thPercentile()), convertDurationStr(snapshot.get999thPercentile()),
                    convertRateStr(timer.getMeanRate()), convertRateStr(timer.getOneMinuteRate()),
                    convertRateStr(timer.getFiveMinuteRate()), convertRateStr(timer.getFifteenMinuteRate()), getRateUnit(),
                    getDurationUnit());
        }
    }

    private void logMeter(String name, Meter meter) {
        long lastCount = getAndSetLastCountFor(name + ".meter", meter.getCount());
        // do not print delta == 0
        if (meter.getCount() - lastCount > 0) {
            loggerProxy.log(marker,
                    "type=METER, name={}, count={}, delta={}, mean_rate={}, m1={}, m5={}, m15={}, rate_unit={}", name,
                    meter.getCount(), meter.getCount() - lastCount, convertRateStr(meter.getMeanRate()),
                    convertRateStr(meter.getOneMinuteRate()), convertRateStr(meter.getFiveMinuteRate()),
                    convertRateStr(meter.getFifteenMinuteRate()), getRateUnit());
        }
    }

    private void logHistogram(String name, Histogram histogram) {
        final Snapshot snapshot = histogram.getSnapshot();
        long lastCount = getAndSetLastCountFor(name + ".hist", histogram.getCount());
        // do not print delta == 0
        if (histogram.getCount() - lastCount > 0) {
            loggerProxy.log(marker, "type=HISTOGRAM, name={}, count={}, delta={}, min={}, max={}, mean={}, stddev={}, "
                            + "median={}, p75={}, p95={}, p98={}, p99={}, p999={}", name, histogram.getCount(),
                    histogram.getCount() - lastCount, snapshot.getMin(), snapshot.getMax(), snapshot.getMean(),
                    snapshot.getStdDev(), snapshot.getMedian(), snapshot.get75thPercentile(), snapshot.get95thPercentile(),
                    snapshot.get98thPercentile(), snapshot.get99thPercentile(), snapshot.get999thPercentile());
        }
    }

    private void logCounter(String name, Counter counter) {
        long lastCount = getAndSetLastCountFor(name + ".counter", counter.getCount());
        // do not print delta == 0
        if (counter.getCount() - lastCount > 0) {
            loggerProxy.log(marker, "type=COUNTER, name={}, count={}, delta={}", name, counter.getCount(),
                    counter.getCount() - lastCount);
        }
    }

    private void logGauge(String name, Gauge gauge) {
        if (gauge.getValue() instanceof Number) {
            Double v = Double.parseDouble(gauge.getValue().toString());
            if (v > 0.0) {
                loggerProxy.log(marker, "type=GAUGE, name={}, value={}", name, gauge.getValue());
            }
        } else {
            loggerProxy.log(marker, "type=GAUGE, name={}, value={}", name, gauge.getValue());
        }
    }

    @Override
    protected String getRateUnit() {
        return "events/" + super.getRateUnit();
    }

    private final ThreadLocal<DecimalFormat> df = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.00");
        }
    };

    protected String convertRateStr(double rate) {
        double r = super.convertRate(rate);
        return df.get().format(r);
    }

    protected Double convertRateDouble(double rate) {
        double r = super.convertRate(rate);
        return Double.parseDouble(df.get().format(r));
    }

    protected String convertDurationStr(double duration) {
        double r = super.convertDuration(duration);
        return df.get().format(r);
    }

    protected Double convertDurationDouble(double duration) {
        double r = super.convertDuration(duration);
        return Double.parseDouble(df.get().format(r));
    }

    /* private class to allow logger configuration */
    static abstract class LoggerProxy {
        protected final Logger logger;

        public LoggerProxy(Logger logger) {
            this.logger = logger;
        }

        abstract void log(Marker marker, String format, Object... arguments);
    }

    /* private class to allow logger configuration */
    private static class DebugLoggerProxy extends LoggerProxy {
        public DebugLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            logger.debug(marker, format, arguments);
        }
    }

    /* private class to allow logger configuration */
    private static class TraceLoggerProxy extends LoggerProxy {
        public TraceLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            logger.trace(marker, format, arguments);
        }

    }

    /* private class to allow logger configuration */
    private static class InfoLoggerProxy extends LoggerProxy {
        public InfoLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            logger.info(marker, format, arguments);
        }
    }

    /* private class to allow logger configuration */
    private static class WarnLoggerProxy extends LoggerProxy {
        public WarnLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            logger.warn(marker, format, arguments);
        }
    }

    /* private class to allow logger configuration */
    private static class ErrorLoggerProxy extends LoggerProxy {
        public ErrorLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(Marker marker, String format, Object... arguments) {
            logger.error(marker, format, arguments);
        }
    }

}
