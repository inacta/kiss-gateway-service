package com.proofx.gateway.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Entity class that represents token data.
 *
 * @author ProofX
 * @since 1.0.0
 */
// @formatter:off
@Table(name = "TAG", indexes = {
        @Index(name = "IDX_CHIP_UUID", columnList = "CHIP_UUID"),
    }
)
// @formatter:on
@Entity
public class TagEntity extends BaseEntity {

    @Column(name = "CHIP_UUID")
    private String uuid;

    @Column(name = "COUNTER")
    private int counter;

    @Column(name = "SECRET_KEY")
    private String secret;

    /**
     * Gets the value of the uuid property.
     *
     * @return possible object is {@link String}
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property
     *
     * @param uuid allowed object is {@link String }
     * @return the {@link TagEntity}
     */
    public TagEntity setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * Gets the value of the counter property.
     *
     * @return possible object is int
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the value of the counter property
     *
     * @param counter allowed object is int
     * @return the {@link TagEntity}
     */
    public TagEntity setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    /**
     * Gets the value of the secret property.
     *
     * @return possible object is {@link String}
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the value of the secret property
     *
     * @param secret allowed object is {@link String }
     * @return the {@link TagEntity}
     */
    public TagEntity setSecret(String secret) {
        this.secret = secret;
        return this;
    }
}
