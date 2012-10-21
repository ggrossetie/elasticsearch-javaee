package org.mogztter.elasticsearch.javaee.common;

/**
 * @author bloemgracht
 */
public class ElasticSearchableEntityEvent {

    private String entityClassName;
    private Long entityId;
    private EntityEventType entityEventType;

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityEventType getEntityEventType() {
        return entityEventType;
    }

    public void setEntityEventType(EntityEventType entityEventType) {
        this.entityEventType = entityEventType;
    }
}
