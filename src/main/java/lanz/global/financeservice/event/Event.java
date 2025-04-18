package lanz.global.financeservice.event;

import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = 4291366152645660782L;

    public UUID eventId;
    public EventTypeEnum type;

    public Map<String, Object> data;

    public Event() {
        this.eventId = UUID.randomUUID();
    }
}
