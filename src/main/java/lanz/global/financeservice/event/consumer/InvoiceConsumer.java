package lanz.global.financeservice.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lanz.global.financeservice.event.Event;
import lanz.global.financeservice.event.producer.InvoiceProducer;
import lanz.global.financeservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceConsumer {

    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = InvoiceProducer.INVOICE_TOPIC, groupId = "invoice-group")
    public void consume(String message) {
        try {
            log.info("Consuming message: {}", message);
            Event event = objectMapper.readValue(message, Event.class);
            UUID contractId = UUID.fromString(String.valueOf(event.data.get("contractId")));

            switch (event.type) {
                case CREATE_INVOICES: {
                    log.info("Event {}: Creating invoices for ContractID: {}", event.eventId.toString(), contractId);
                    invoiceService.createInvoices(contractId);
                    log.info("Event {}: Created invoices for ContractID: {}", event.eventId.toString(), contractId);
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + event.type);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
