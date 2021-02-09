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
@Entity
// @formatter:on
public class TagEntity extends BaseEntity {

    @Column(name = "CHIP_UUID")
    private String chipUUID;

    @Column(name = "COUNTER")
    private int counter;

    @Column(name = "SECRET_KEY")
    private String secretKey;

    /**
     * Gets the value of the secretKey property.
     *
     * @return possible object is {@link String}
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Sets the value of the secretKey property
     *
     * @param secretKey allowed object is {@link String }
     * @return the {@link TagEntity}
     */
    public TagEntity setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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
     * Gets the value of the chipUUID property.
     *
     * @return possible object is {@link String}
     */
    public String getChipUUID() {
        return chipUUID;
    }

    /**
     * Sets the value of the chipUUID property
     *
     * @param chipUUID allowed object is {@link String }
     * @return the {@link TagEntity}
     */
    public TagEntity setChipUUID(String chipUUID) {
        this.chipUUID = chipUUID;
        return this;
    }
}
