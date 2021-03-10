package com.proofx.gateway.api.v1.model.humanity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * webhook request
 *
 * @author ProofX
 * @since 1.0.0
 */
@SuppressWarnings({"java:S100", "java:S116", "java:S117"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookRequest {
    String email;
    String created_at;
    Integer order_number;
    List<LineItem> line_items;

    /**
     * Gets the value of the email property.
     *
     * @return possible object is {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property
     *
     * @param email allowed object is {@link String }
     * @return the {@link WebhookRequest}
     */
    public WebhookRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Gets the value of the created_at property.
     *
     * @return possible object is {@link String}
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Sets the value of the created_at property
     *
     * @param created_at allowed object is {@link String }
     * @return the {@link WebhookRequest}
     */
    public WebhookRequest setCreated_at(String created_at) {
        this.created_at = created_at;
        return this;
    }

    /**
     * Gets the value of the order_number property.
     *
     * @return possible object is long
     */
    public long getOrder_number() {
        return order_number;
    }

    /**
     * Sets the value of the order_number property
     *
     * @param order_number allowed object is long
     * @return the {@link WebhookRequest}
     */
    public WebhookRequest setOrder_number(Integer order_number) {
        this.order_number = order_number;
        return this;
    }

    /**
     * Gets the value of the line_items property.
     *
     * @return possible object is {@link List< LineItem>}
     */
    public List<LineItem> getLine_items() {
        return line_items;
    }

    /**
     * Sets the value of the line_items property
     *
     * @param line_items allowed object is {@link List< LineItem> }
     * @return the {@link WebhookRequest}
     */
    public WebhookRequest setLine_items(List<LineItem> line_items) {
        this.line_items = line_items;
        return this;
    }
}
