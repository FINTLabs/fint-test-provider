package no.fint.provider.subscriber;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.events.FintEvents;
import no.fint.events.FintEventsHealth;
import no.fint.events.annotations.FintEventListener;
import no.fint.events.queue.QueueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DownstreamSubscriber {

    @Autowired(required = false)
    private FintEvents fintEvents;

    @Autowired(required = false)
    private FintEventsHealth fintEventsHealth;

    @FintEventListener(type = QueueType.DOWNSTREAM)
    public void receive(Event event) {
        if (fintEvents != null && fintEventsHealth != null) {
            log.info("Received event: {}", event);
            event.setStatus(Status.UPSTREAM_QUEUE);

            if (event.isHealthCheck()) {
                event.addObject(new Health("test-provider", HealthStatus.APPLICATION_HEALTHY));
                fintEventsHealth.respondHealthCheck(event.getCorrId(), event);
            } else {
                fintEvents.sendUpstream(event.getOrgId(), event);
            }
        }
    }
}
