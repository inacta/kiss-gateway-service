package com.proofx.gateway.api.v1.model.nodeserver;

/**
 * get balance response
 *
 * @author ProofX
 * @since 1.0.0
 */
public class GetBalanceResponse {
    private String balance;

    /**
     * Gets the value of the balance property.
     *
     * @return possible object is {@link String}
     */
    public String getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property
     *
     * @param balance allowed object is {@link String }
     * @return the {@link GetBalanceResponse}
     */
    public GetBalanceResponse setBalance(String balance) {
        this.balance = balance;
        return this;
    }
}
