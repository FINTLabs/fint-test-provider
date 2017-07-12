package no.fint.provider.sse

import spock.lang.Specification

class SseServiceSpec extends Specification {
    private SseService sseService

    void setup() {
        sseService = new SseService()
    }

    def "Register and return SSE emitter"() {
        when:
        def emitter = sseService.registerClient('123')

        then:
        emitter != null
    }

    def "Returns registered SSE client ids"() {
        given:
        sseService.registerClient('123')

        when:
        def clients = sseService.getRegisteredClientIds()

        then:
        clients.size() == 1
    }

    def "Returns registered SSE clients"() {
        given:
        sseService.registerClient('123')

        when:
        def clients = sseService.getRegisteredClients()

        then:
        clients.size() == 1
    }

    def "Delete all registered clients"() {
        given:
        sseService.registerClient('123')

        when:
        sseService.removeRegisteredClients()
        def clients = sseService.getRegisteredClients()

        then:
        clients.size() == 0
    }

}
