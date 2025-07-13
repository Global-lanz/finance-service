package lanz.global.financeservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.repository.ContractFilterRepository;
import lanz.global.financeservice.repository.util.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ContractFilterRepositoryImpl extends AbstractRepository implements ContractFilterRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    @Override
    public Page<Contract> findAllByFilter(UUID companyId, GetContractParams params) {
        CriteriaQuery<Contract> query = criteriaQuery(Contract.class);
        Root<Contract> from = from(query, Contract.class);
        Predicate predicate = filter(params, from);

        query.where(predicate);
        sort(query, from, "contractId");

        return pageable(params, predicate, query, Contract.class);
    }

    private Predicate filter(GetContractParams params, Root<Contract> from) {
        List<Predicate> filter = new ArrayList<>();
        if (params.getCustomerId() != null) {
            filter.add(equal(from, "customerId", params.getCustomerId()));
        }
        return getCriteriaBuilder().and(filter.toArray(new Predicate[0]));
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder() {
        if (criteriaBuilder == null) {
            criteriaBuilder = entityManager.getCriteriaBuilder();
        }
        return criteriaBuilder;
    }
}
