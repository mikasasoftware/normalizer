package rb.ks.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rb.ks.builder.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ConsoleMetricListener implements MetricListener {
    private static final Logger log = LoggerFactory.getLogger(ConsoleMetricListener.class);
    ObjectMapper mapper;

    @Override
    public void init(Config config) {
        mapper = new ObjectMapper();
    }

    @Override
    public void updateMetric(String metricName, Object metricValue) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("timestamp", System.currentTimeMillis() / 1000L);
        metric.put("metric", metricName);
        metric.put("value", metricValue);

        try {
            log.info(mapper.writeValueAsString(metric));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        //Nothing to do
    }

    @Override
    public String name() {
        return "console";
    }
}
