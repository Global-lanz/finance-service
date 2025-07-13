package lanz.global.financeservice.api.response.contract;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.converters.models.Pageable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ContractPageResponse extends Pageable implements Serializable {

    @Serial
    private static final long serialVersionUID = 7142606946855707319L;

    private List<ContractResponse> contracts;

    public ContractPageResponse(int page, int size, List<ContractResponse> contracts) {
        super(page, size, null);
        this.contracts = contracts;
    }
}
