package com.proofx.gateway.core.jpa;

import com.proofx.gateway.core.jpa.listeners.DatabaseMetaDataListener;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Base Entity class which provides basic fields and id generation definition.
 *
 * @author ProofX
 * @since 1.0.0
 */
// @formatter:off
@MappedSuperclass
// @formatter:on
@EntityListeners({ DatabaseMetaDataListener.class })
public abstract class BaseEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = { @Parameter(name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    @Column(name = "ID",
            updatable = false,
            nullable = false)
    private String id;

    @Column(name = "CREATED",
            nullable = false)
    private LocalDateTime created;

    @Column(name = "LAST_UPDATE",
            nullable = false)
    private LocalDateTime lastUpdate;

    /**
     * Gets the value of the created property.
     *
     * @return possible object is {@link LocalDateTime}
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property
     *
     * @param created allowed object is {@link LocalDateTime }
     * @return the {@link BaseEntity}
     */
    public BaseEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    /**
     * Gets the value of the lastUpdate property.
     *
     * @return possible object is {@link LocalDateTime}
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the value of the lastUpdate property
     *
     * @param lastUpdate allowed object is {@link LocalDateTime }
     * @return the {@link BaseEntity}
     */
    public BaseEntity setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property
     *
     * @param id allowed object is {@link String }
     * @return the {@link BaseEntity}
     */
    public BaseEntity setId(String id) {
        this.id = id;
        return this;
    }
}
