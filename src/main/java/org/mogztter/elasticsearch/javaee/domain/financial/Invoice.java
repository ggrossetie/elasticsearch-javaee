package org.mogztter.elasticsearch.javaee.domain.financial;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.mogztter.elasticsearch.javaee.common.ElasticSearchable;

/**
 * @author bloemgracht
 */
@Entity
public class Invoice implements ElasticSearchable {

    private Long id;
    private String number;
    private Date date;
    private Long supplierId;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
