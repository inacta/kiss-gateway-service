package ch.inacta.kiss.gateway.api.v1.model.nodeserver;

/**
 * CheckAddressResponse
 *
 * @author ProofX
 * @since 1.0.0
 */
public class CheckAddressResponse {
    private boolean valid;

    /**
     * Gets the value of the valid property.
     *
     * @return possible object is boolean
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the value of the valid property
     *
     * @param valid allowed object is boolean
     * @return the {@link CheckAddressResponse}
     */
    public CheckAddressResponse setValid(boolean valid) {
        this.valid = valid;
        return this;
    }
}
