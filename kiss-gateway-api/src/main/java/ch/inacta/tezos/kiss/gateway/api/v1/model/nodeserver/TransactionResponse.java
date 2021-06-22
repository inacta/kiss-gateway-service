package ch.inacta.tezos.kiss.gateway.api.v1.model.nodeserver;

/**
 * transaction response
 *
 * @author ProofX
 * @since 1.0.0
 */
public class TransactionResponse {
    private TransferStatus status;
    private String message;
    private String from;
    private String txHash;
    private String gasPrice;
    private String gasUsed;
    private String gasLimit;

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link TransferStatus}
     */
    public TransferStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property
     *
     * @param status allowed object is {@link TransferStatus }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setStatus(TransferStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is {@link String}
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property
     *
     * @param message allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Gets the value of the from property.
     *
     * @return possible object is {@link String}
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property
     *
     * @param from allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setFrom(String from) {
        this.from = from;
        return this;
    }

    /**
     * Gets the value of the txHash property.
     *
     * @return possible object is {@link String}
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * Sets the value of the txHash property
     *
     * @param txHash allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setTxHash(String txHash) {
        this.txHash = txHash;
        return this;
    }

    /**
     * Gets the value of the gasPrice property.
     *
     * @return possible object is {@link String}
     */
    public String getGasPrice() {
        return gasPrice;
    }

    /**
     * Sets the value of the gasPrice property
     *
     * @param gasPrice allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    /**
     * Gets the value of the gasUsed property.
     *
     * @return possible object is {@link String}
     */
    public String getGasUsed() {
        return gasUsed;
    }

    /**
     * Sets the value of the gasUsed property
     *
     * @param gasUsed allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    /**
     * Gets the value of the gasLimit property.
     *
     * @return possible object is {@link String}
     */
    public String getGasLimit() {
        return gasLimit;
    }

    /**
     * Sets the value of the gasLimit property
     *
     * @param gasLimit allowed object is {@link String }
     * @return the {@link TransactionResponse}
     */
    public TransactionResponse setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }
}
