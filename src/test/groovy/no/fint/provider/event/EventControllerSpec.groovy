package no.fint.provider.event

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.event.model.DefaultActions
import no.fint.event.model.Event
import no.fint.event.model.HeaderConstants
import no.fint.provider.sse.SseService
import no.fint.test.utils.MockMvcSpecification
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

class EventControllerSpec extends MockMvcSpecification {
    private SseService sseService
    private EventController controller
    private MockMvc mockMvc

    private String eventJson

    void setup() {
        sseService = Mock(SseService)
        controller = new EventController(sseService: sseService)
        mockMvc = standaloneSetup(controller)

        def event = new Event('rogfk.no', 'test', DefaultActions.HEALTH, 'test')
        eventJson = new ObjectMapper().writeValueAsString(event)
    }

    def "Send event"() {
        when:
        def response = mockMvc.perform(post('/event'))

        then:
        1 * sseService.getRegisteredClients() >> ['fint.health': new SseEmitter()]
        response.andExpect(status().isOk())
    }

    def "Receive adapter status"() {
        when:
        def response = mockMvc.perform(post('/status')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .content(eventJson))

        then:
        response.andExpect(status().isOk())
    }

    def "Receive adapter response"() {
        when:
        def response = mockMvc.perform(post('/response')
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HeaderConstants.ORG_ID, 'rogfk.no')
                .content(eventJson))

        then:
        response.andExpect(status().isOk())
    }

}
