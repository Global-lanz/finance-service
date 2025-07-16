package lanz.global.financeservice.repository.specification;

import lanz.global.financeservice.model.Contract;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ContractSpecification {

    private ContractSpecification() {
    }

    public static Specification<Contract> customerId(UUID customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("customerId"), customerId);
        };
    }

    public static Specification<Contract> companyId(UUID companyId) {
        return (root, query, criteriaBuilder) -> {
            if (companyId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("companyId"), companyId);
        };
    }


}
