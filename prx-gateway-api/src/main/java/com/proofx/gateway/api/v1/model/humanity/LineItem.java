package com.proofx.gateway.api.v1.model.humanity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * line item
 *
 * @author ProofX
 * @since 1.0.0
 */
@SuppressWarnings({"java:S100", "java:S116", "java:S117"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {
    Integer quantity;
    Long product_id;
    String price;

    /**
     * Gets the value of the quantity property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property
     *
     * @param quantity allowed object is {@link Integer }
     * @return the {@link LineItem}
     */
    public LineItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Gets the value of the product_id property.
     *
     * @return possible object is {@link Long}
     */
    public Long getProduct_id() {
        return product_id;
    }

    /**
     * Sets the value of the product_id property
     *
     * @param product_id allowed object is {@link Long }
     * @return the {@link LineItem}
     */
    public LineItem setProduct_id(Long product_id) {
        this.product_id = product_id;
        return this;
    }

    /**
     * Gets the value of the price property.
     *
     * @return possible object is {@link String}
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property
     *
     * @param price allowed object is {@link String }
     * @return the {@link LineItem}
     */
    public LineItem setPrice(String price) {
        this.price = price;
        return this;
    }
}
