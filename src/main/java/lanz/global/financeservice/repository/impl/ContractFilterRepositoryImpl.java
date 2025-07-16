package lanz.global.financeservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.repository.ContractFilterRepository;
import lanz.global.financeservice.repository.util.AbstractRepository;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContractFilterRepositoryImpl extends AbstractRepository implements ContractFilterRepository {


    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Page<Contract> findAllByFilter(UUID companyId, GetContractParams params) {
        CriteriaQuery<Contract> query = criteriaQuery(Contract.class);
        Root<Contract> from = from(query, Contract.class);

        List<Predicate> predicates = getFilterList(params, from);
        Predicate predicate = predicate(predicates);

        query.where(predicate);
        sort(query, from, "contractId");

        return pageable(params, predicates, query, Contract.class);
    }

    private List<Predicate> getFilterList(GetContractParams params, Root<Contract> from) {
        List<Predicate> predicates = new ArrayList<>();
        if (params.getCustomerId() != null) {
            predicates.add(equal(from, "customerId", params.getCustomerId()));
        }
        return predicates;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManagerFactory.getCriteriaBuilder();
    }
}
