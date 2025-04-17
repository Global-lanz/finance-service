package lanz.global.financeservice.event.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lanz.global.financeservice.event.Event;
import lanz.global.financeservice.event.EventTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceProducer {

    public static final String INVOICE_TOPIC = "invoice-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void createInvoices(UUID contractId) {
        Event event = new Event();
        event.type = EventTypeEnum.CREATE_INVOICES;
        event.data = Map.of("contractId", contractId);
        sendEvent(event);
    }

    private void sendEvent(Event event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(INVOICE_TOPIC, message);
            log.info("Event sent!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
