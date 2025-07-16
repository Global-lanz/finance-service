package lanz.global.financeservice.repository.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class AbstractRepository<T> {

    protected CriteriaQuery<T> criteriaQuery(Class<T> entityClass) {
        return getCriteriaBuilder().createQuery(entityClass);
    }

    protected Root<T> from(CriteriaQuery<T> criteriaQuery, Class<T> entityClass) {
        return criteriaQuery.from(entityClass);
    }

    protected void sort(CriteriaQuery<T> criteriaQuery, Root<T> from, String attributeName) {
        criteriaQuery.orderBy(getCriteriaBuilder().asc(from.get(attributeName)));
    }

    protected Page<T> pageable(Pageable params, List<Predicate> predicates, CriteriaQuery<T> query, Class<T> entityClass) {
        TypedQuery<T> typedQuery = getEntityManager().createQuery(query);
        typedQuery.setFirstResult(params.getPage() * params.getSize());
        typedQuery.setMaxResults(params.getSize());

        Sort sort = Sort.by(Sort.Direction.ASC, params.getSort().toArray(new String[0]));
        PageRequest pageRequest = PageRequest.of(params.getPage(), params.getSize(), sort);

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, countByFilter(predicates, entityClass));
    }

    protected long countByFilter(List<Predicate> predicates, Class<T> entityClass) {
        CriteriaQuery<Long> countQuery = getCriteriaBuilder().createQuery(Long.class);
        Root<T> from = countQuery.from(entityClass);
        countQuery.select(getCriteriaBuilder().count(from)).where(predicate(predicates));
        return getEntityManager().createQuery(countQuery).getSingleResult();
    }

    protected Predicate predicate(List<Predicate> predicates) {
        return getCriteriaBuilder().and(predicates.toArray(new Predicate[0]));
    }

    protected Predicate equal(Path<T> from, String attributeName, Object parameter) {
        getCriteriaBuilder().equal(from.get(attributeName), parameter);
        return getCriteriaBuilder().equal(from.get(attributeName), parameter);
    }

    protected abstract EntityManager getEntityManager();

    protected abstract CriteriaBuilder getCriteriaBuilder();

}
