package no.fint.provider.sse

import no.fint.test.utils.MockMvcSpecification
import org.springframework.test.web.servlet.MockMvc

class SseControllerSpec extends MockMvcSpecification {
    private MockMvc mockMvc
    private SseController sseController
    private SseService sseService

    void setup() {
        sseService = Mock(SseService)
        sseController = new SseController(sseService: sseService)
        mockMvc = standaloneSetup(sseController)
    }

    def "Returns status OK when subscribing SSE client"() {
        when:
        def response = mockMvc.perform(get('/sse/123'))

        then:
        response.andExpect(status().isOk())
    }

    def "Returns subscribed SSE client"() {
        given:
        mockMvc.perform(get('/sse/123'))

        when:
        def response = mockMvc.perform(get('/sse/clients'))

        then:
        1 * sseService.getRegisteredClientIds() >> ['123']
        response.andExpect(jsonPathSize('$', 1))
                .andExpect(jsonPathEquals('$[0]', '123'))
    }

    def "Return status OK when deleting registered clients"() {
        when:
        def response = mockMvc.perform(delete('/sse/clients'))

        then:
        1 * sseService.removeRegisteredClients()
        response.andExpect(status().isOk())
    }

}
