package io.github.hengyunabc.metrics.test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.codahale.metrics.MetricRegistry;

import io.github.hengyunabc.metrics.KafkaReporter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
public class SpringTest {

    @Autowired
    AnnotationObject annotationObject;

    @Autowired
    MetricRegistry metrics;

    @Before
    public void before() {
        startKafkaReporter();
    }

    public void startKafkaReporter() {
        String hostName = "localhost";
        String topic = "test-kafka-reporter";
        Properties config = new Properties();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", StringSerializer.class.getName());
        config.put("value.serializer", ByteArraySerializer.class.getName());

        String prefix = "test.";
        KafkaReporter kafkaReporter =
                KafkaReporter.forRegistry(metrics).config(config).topic(topic).hostName(hostName).prefix(prefix)
                        .build();

        kafkaReporter.start(3, TimeUnit.SECONDS);
    }

    @Ignore
    @Test
    public void test() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    annotationObject.call();
                    annotationObject.userLogin();
                }
            }
        });
        t.start();

        TimeUnit.SECONDS.sleep(500);
    }
}
