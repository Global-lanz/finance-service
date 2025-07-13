package lanz.global.financeservice.api.request.contract;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.converters.models.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetContractParams extends Pageable {

    private UUID customerId;

    public GetContractParams(int page, int size, List<String> sort) {
        super(page, size, sort);
    }

    public GetContractParams() {
        super(0, 0, Collections.emptyList());
    }
}