package no.fint.provider.event;

import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import no.fint.provider.sse.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
public class EventController {

    @Autowired
    private SseService sseService;

    private volatile Event receivedEvent = null;

    @PostMapping("/event")
    public Event sendEvent(@RequestParam(defaultValue = "HEALTH") String action,
                           @RequestParam(defaultValue = "mock.no") String orgId,
                           @RequestParam(required = false) String query) {
        Event event = new Event(orgId, "test-provider", action, "test-client");
        if (!StringUtils.isEmpty(query)) {
            event.setQuery(query);
        }

        sendEvent(event);
        return getAdapterResponseEvent();
    }

    private void sendEvent(Event event) {
        Map<String, SseEmitter> clients = sseService.getRegisteredClients();
        SseEmitter.SseEventBuilder builder = SseEmitter.event().id(event.getCorrId()).name(event.getAction()).data(event).reconnectTime(5000L);
        clients.values().forEach(emitter -> {
            try {
                emitter.send(builder);
            } catch (IOException e) {
                log.error("Unable to send event to SSE client", e);
            }
        });
    }

    private Event getAdapterResponseEvent() {
        int counter = 0;
        while (receivedEvent == null && counter < 50) {
            counter++;
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ignored) {
            }
        }

        if(receivedEvent == null) {
            log.warn("No response from adapter");
            return null;
        } else {
            Event eventToBeReturned = receivedEvent;
            receivedEvent = null;
            return eventToBeReturned;
        }
    }

    @PostMapping("/status")
    public void postStatus(@RequestHeader(HeaderConstants.ORG_ID) String orgId,
                           @RequestBody Event event) {
        log.info("/status called with orgId:{} and event:{}", orgId, event);
    }

    @PostMapping("/response")
    public void postResponse(@RequestHeader(HeaderConstants.ORG_ID) String orgId,
                             @RequestBody Event event) {
        log.info("/response called with orgId:{} and event:{}", orgId, event);
        receivedEvent = event;
    }
}
