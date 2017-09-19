package no.fint.provider;

import no.fint.events.annotations.EnableFintEvents;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty("fint.consumer.enabled")
@Configuration
@EnableFintEvents
public class ConsumerConfig {
}
