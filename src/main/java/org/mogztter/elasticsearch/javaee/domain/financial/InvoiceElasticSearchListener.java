package org.mogztter.elasticsearch.javaee.domain.financial;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.mogztter.elasticsearch.javaee.common.ElasticSearchableEntityEvent;
import org.mogztter.elasticsearch.javaee.domain.customer.Supplier;

/**
 * @author bloemgracht
 */
@Stateless
public class InvoiceElasticSearchListener {

    @PersistenceContext
    private EntityManager em;

    public void listenToElasticSearchableEntity(@Observes ElasticSearchableEntityEvent elasticSearchableEntityEvent) throws IOException {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        try {
            final Long entityId = elasticSearchableEntityEvent.getEntityId();
            if (Invoice.class.getName().equals(elasticSearchableEntityEvent.getEntityClassName())) {
                final Invoice invoice = em.find(Invoice.class, entityId);
                final Supplier supplier = em.find(Supplier.class, invoice.getSupplierId());
                client.prepareIndex("financial", "invoice", entityId.toString())
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("number", invoice.getNumber())
                                .field("date", invoice.getDate())
                                .field("supplier.name", supplier.getName())
                                .field("supplier.number", supplier.getNumber())
                                .endObject()
                        )
                        .execute()
                        .actionGet();
            } else if (Supplier.class.getName().equals(elasticSearchableEntityEvent.getEntityClassName())) {
                final Supplier supplier = em.find(Supplier.class, entityId);
                List<Invoice> invoices = findInvoicesBySupplierId(entityId);
                for (Invoice invoice : invoices) {
                    client.prepareUpdate("financial", "invoice", invoice.getId().toString())
                            .setScript("ctx._source.supplier.name=\"" + supplier.getName() + "\"")
                            .setScript("ctx._source.supplier.number=\"" + supplier.getNumber() + "\"")
                            .execute()
                            .actionGet();
                }
            }
        } finally {
            client.close();
        }
    }

    private List<Invoice> findInvoicesBySupplierId(Long entityId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteriaQuery = cb.createQuery(Invoice.class);
        Root<Invoice> c = criteriaQuery.from(Invoice.class);
        criteriaQuery.select(c).where(cb.equal(c.get("supplierId"), entityId));
        TypedQuery<Invoice> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
