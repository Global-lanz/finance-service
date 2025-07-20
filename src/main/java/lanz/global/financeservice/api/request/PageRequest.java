package lanz.global.financeservice.api.request;

import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;

@Setter
public class PageRequest implements Pageable, Serializable {

    @Serial
    private static final long serialVersionUID = 910997642915720053L;

    private int pageNumber;
    private int pageSize;
    private Sort sort;

    public PageRequest(int pageNumber, int pageSize, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }


    @Override
    public int getPageSize() {
        return pageSize;
    }


    @Override
    public long getOffset() {
        return (long) this.pageNumber * (long) this.pageSize;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new PageRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return this.getPageNumber() == 0 ? this : new PageRequest(this.getPageNumber() - 1, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable first() {
        return new PageRequest(0, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new PageRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.pageNumber > 0;
    }
}
