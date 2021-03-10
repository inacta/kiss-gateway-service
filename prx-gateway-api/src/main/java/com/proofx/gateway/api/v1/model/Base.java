package com.proofx.gateway.api.v1.model;

/**
 * Base
 *
 * @author ProofX
 * @since 1.0.0
 */
public class Base {
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
     * @return the {@link Base}
     */
    public Base setId(String id) {
        this.id = id;
        return this;
    }
}
