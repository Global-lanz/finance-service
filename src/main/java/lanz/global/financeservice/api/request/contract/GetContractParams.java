package lanz.global.financeservice.api.request.contract;

import lanz.global.financeservice.api.request.PageRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
public class GetContractParams extends PageRequest {

    @Serial
    private static final long serialVersionUID = -3981791853063930213L;

    private UUID customerId;

    public GetContractParams() {
        super(0, 10, Sort.unsorted());
    }

    public GetContractParams(UUID customerId) {
        super(0, 10, Sort.unsorted());
        this.customerId = customerId;
    }
}