package org.mogztter.elasticsearch.javaee.common;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author bloemgracht
 */
@Stateless
public class GenericDao<T extends ElasticSearchable> {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Event<ElasticSearchableEntityEvent> events;

    public void persist(T entity) {
        em.persist(entity);
        ElasticSearchableEntityEvent event = createElasticSearchableEntityEvent(entity, EntityEventType.PERSIST);
        events.fire(event);
    }

    public T merge(T entity) {
        final T merge = em.merge(entity);
        ElasticSearchableEntityEvent event = createElasticSearchableEntityEvent(entity, EntityEventType.MERGE);
        events.fire(event);
        return merge;
    }

    public void remove(T entity) {
        em.remove(entity);
        ElasticSearchableEntityEvent event = createElasticSearchableEntityEvent(entity, EntityEventType.REMOVE);
        events.fire(event);
    }

    private ElasticSearchableEntityEvent createElasticSearchableEntityEvent(ElasticSearchable entity, EntityEventType entityEventType) {
        ElasticSearchableEntityEvent event = new ElasticSearchableEntityEvent();
        event.setEntityClassName(entity.getClass().getName());
        event.setEntityId(entity.getId());
        event.setEntityEventType(entityEventType);
        return event;
    }

}
