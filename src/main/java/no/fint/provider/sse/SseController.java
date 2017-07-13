package no.fint.provider.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseService sseService;

    @ApiIgnore
    @GetMapping("/{id}")
    public SseEmitter subscribe(@PathVariable String id) {
        log.info("Connecting sse client:{}", id);
        return sseService.registerClient(id);
    }

    @GetMapping("/clients")
    public Set<String> getRegisteredClients() {
        return sseService.getRegisteredClientIds();
    }

    @DeleteMapping("/clients")
    public void removeRegisteredClients() {
        sseService.removeRegisteredClients();
    }


}
