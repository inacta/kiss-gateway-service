package ch.inacta.tezos.kiss.gateway.api.v1.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * Base
 *
 * @author ProofX
 * @since 1.0.0
 */
public class Base {
    private String id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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
     * @return the {@link Base}
     */
    public Base setCreated(LocalDateTime created) {
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
     * @return the {@link Base}
     */
    public Base setLastUpdate(LocalDateTime lastUpdate) {
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
     * @return the {@link Base}
     */
    public Base setId(String id) {
        this.id = id;
        return this;
    }
}
