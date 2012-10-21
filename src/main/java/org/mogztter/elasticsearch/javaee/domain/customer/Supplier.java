package org.mogztter.elasticsearch.javaee.domain.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.mogztter.elasticsearch.javaee.common.ElasticSearchable;

/**
 * @author bloemgracht
 */
@Entity
public class Supplier implements ElasticSearchable {

    private Long id;
    private String number;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
