package org.mogztter.elasticsearch.javaee.run;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mogztter.elasticsearch.javaee.common.GenericDao;
import org.mogztter.elasticsearch.javaee.domain.customer.Supplier;
import org.mogztter.elasticsearch.javaee.domain.financial.Invoice;

/**
 * @author bloemgracht
 */
@Singleton
@Startup
public class InitData {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private GenericDao<Supplier> supplierDao;
    @Inject
    private GenericDao<Invoice> invoiceDao;

    @PostConstruct
    public void postConstruct() {
        // -- Supplier
        Supplier supplier = new Supplier();
        supplier.setName("Jean-Paul");
        supplier.setNumber("123");
        supplierDao.persist(supplier);

       supplier = new Supplier();
        supplier.setName("Michèle");
        supplier.setNumber("456");
        supplierDao.persist(supplier);

        supplier = new Supplier();
        supplier.setName("Jean-Marc");
        supplier.setNumber("789");
        supplierDao.persist(supplier);

        supplier = new Supplier();
        supplier.setName("Léo'paul");
        supplier.setNumber("741");
        supplierDao.persist(supplier);

        // -- Invoice
        Invoice invoice = new Invoice();
        invoice.setDate(new Date());
        invoice.setNumber("abc");
        supplier = em.createQuery("FROM Supplier s WHERE s.number = '123'", Supplier.class).getSingleResult();
        invoice.setSupplierId(supplier.getId());
        invoiceDao.persist(invoice);

        invoice = new Invoice();
        invoice.setDate(new Date());
        invoice.setNumber("efg");
        supplier = em.createQuery("FROM Supplier s WHERE s.number = '456'", Supplier.class).getSingleResult();
        invoice.setSupplierId(supplier.getId());
        invoiceDao.persist(invoice);

        invoice = new Invoice();
        invoice.setDate(new Date());
        invoice.setNumber("hij");
        supplier = em.createQuery("FROM Supplier s WHERE s.number = '789'", Supplier.class).getSingleResult();
        invoice.setSupplierId(supplier.getId());
        invoiceDao.persist(invoice);

        invoice = new Invoice();
        invoice.setDate(new Date());
        invoice.setNumber("klm");
        supplier = em.createQuery("FROM Supplier s WHERE s.number = '741'", Supplier.class).getSingleResult();
        invoice.setSupplierId(supplier.getId());
        invoiceDao.persist(invoice);
    }
}
