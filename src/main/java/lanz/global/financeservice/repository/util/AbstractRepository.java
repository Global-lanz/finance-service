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

public abstract class AbstractRepository<T> {

    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;

    protected AbstractRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    protected CriteriaQuery<T> criteriaQuery(Class<T> entityClass) {
        return criteriaBuilder.createQuery(entityClass);
    }

    protected Root<T> from(CriteriaQuery<T> criteriaQuery, Class<T> entityClass) {
        return criteriaQuery.from(entityClass);
    }

    protected void sort(CriteriaQuery<T> criteriaQuery, Root<T> from, String attributeName) {
        criteriaQuery.orderBy(criteriaBuilder.asc(from.get(attributeName)));
    }

    protected Page<T> pageable(Pageable params, Predicate predicate, CriteriaQuery<T> query, Class<T> entityClass) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(params.getPage() * params.getSize());
        typedQuery.setMaxResults(params.getSize());

        Sort sort = Sort.by(Sort.Direction.ASC, params.getSort().toArray(new String[0]));
        PageRequest pageRequest = PageRequest.of(params.getPage(), params.getSize(), sort);

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, countByFilter(predicate, entityClass));
    }

    protected long countByFilter(Predicate predicate, Class<T> entityClass) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> from = countQuery.from(entityClass);
        countQuery.select(criteriaBuilder.count(from)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    protected Predicate equal(Path<T> from, String attributeName, Object parameter) {
        criteriaBuilder.equal(from.get(attributeName), parameter);
        return criteriaBuilder.equal(from.get(attributeName), parameter);
    }
}
