package io.wizzie.normalizer.funcs.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wizzie.bootstrapper.builder.Config;
import io.wizzie.normalizer.builder.StreamBuilder;
import io.wizzie.normalizer.exceptions.PlanBuilderException;
import io.wizzie.normalizer.funcs.Function;
import io.wizzie.normalizer.funcs.MapperFunction;
import io.wizzie.normalizer.model.PlanModel;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LowerCaseStringMapperUnitTest {

    static Config config = new Config();

    static {
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "app-id-1");
    }

    private static StreamBuilder streamBuilder = new StreamBuilder(config, null);

    @BeforeClass
    public static void initTest() throws IOException, PlanBuilderException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("lower-string-mapper.json").getFile());

        ObjectMapper objectMapper = new ObjectMapper();
        PlanModel model = objectMapper.readValue(file, PlanModel.class);
        streamBuilder.builder(model);
    }

    @Test
    public void building() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        assertEquals("mac", myLowerCaseStringFunction.dimension);
    }

    @Test
    public void processSimpleMessage() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        Map<String, Object> msg = new HashMap<>();
        msg.put("timestamp", 1234567890);
        msg.put("mac", "Ab1231cEf");

        Map<String, Object> expectedMsg = new HashMap<>();
        expectedMsg.put("timestamp", 1234567890);
        expectedMsg.put("mac", "ab1231cef");

        assertEquals(new KeyValue<>("KEY-A", expectedMsg), myLowerCaseStringFunction.process("KEY-A", msg));
    }

    @Test
    public void processNullKey() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        List<Integer> measures = Arrays.asList(2, 1, 0, 8, 4, 3);

        Map<String, Object> msg = new HashMap<>();
        msg.put("timestamp", 1234567890);
        msg.put("mac", "Ab1231cEf");

        Map<String, Object> expectedMsg = new HashMap<>();
        expectedMsg.put("timestamp", 1234567890);
        expectedMsg.put("mac", "ab1231cef");

        assertEquals(new KeyValue<>(null, expectedMsg), myLowerCaseStringFunction.process(null, msg));
    }

    @Test
    public void processNullMessages() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        List<Integer> measures = Arrays.asList(2, 1, 0, 8, 4, 3);

        assertEquals(new KeyValue<>("key", null), myLowerCaseStringFunction.process("key", null));
    }

    @Test
    public void processNullKeysAndMessages() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        assertEquals(new KeyValue<>(null, null), myLowerCaseStringFunction.process(null, null));
    }

    @Test
    public void processNullDimensionMessages() {
        Map<String, Function> functions = streamBuilder.getFunctions("myStream");
        Function myFunc = functions.get("myLowerStringMapper");

        assertNotNull(myFunc);
        assertTrue(myFunc instanceof MapperFunction);
        LowerCaseStringMapper myLowerCaseStringFunction = (LowerCaseStringMapper) myFunc;

        List<Integer> measures = Arrays.asList(2, 1, 0, 8, 4, 3);

        Map<String, Object> msg = new HashMap<>();
        msg.put("timestamp", 1234567890);

        Map<String, Object> expectedMsg = new HashMap<>();
        expectedMsg.put("timestamp", 1234567890);

        assertEquals(new KeyValue<>(null, expectedMsg), myLowerCaseStringFunction.process(null, msg));
    }

}
