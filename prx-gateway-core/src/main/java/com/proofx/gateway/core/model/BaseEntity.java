package com.proofx.gateway.core.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base Entity class which provides basic fields and id generation definition.
 *
 * @author ProofX
 * @since 1.0.0
 */
// @formatter:off
@MappedSuperclass
// @formatter:on
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
