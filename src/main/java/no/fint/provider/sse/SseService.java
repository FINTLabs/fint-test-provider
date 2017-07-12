package no.fint.provider.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class SseService {
    private static final long TIMEOUT = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter registerClient(String id) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(id, emitter);
        return emitter;
    }

    public Map<String, SseEmitter> getRegisteredClients() {
        return emitters;
    }

    public Set<String> getRegisteredClientIds() {
        return emitters.keySet();
    }

    public void removeRegisteredClients() {
        emitters.clear();
    }
}
