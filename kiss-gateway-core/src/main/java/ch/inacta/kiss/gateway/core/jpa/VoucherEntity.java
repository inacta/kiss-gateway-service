package ch.inacta.kiss.gateway.core.jpa;

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
@Table(name = "VOUCHERS", indexes = {
        @Index(name = "IDX_VOUCHER", columnList = "VOUCHER"),
    }
)
// @formatter:on
@SuppressWarnings({"java:S100", "java:S116", "java:S117"})
@Entity
public class VoucherEntity extends BaseEntity {

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ORDER_NUMBER")
    private Integer order_number;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "VOUCHER")
    private String voucher;

    @Column(name = "EMAIL_SENT")
    private Boolean email_sent;

    @Column(name = "ACTIVE")
    private Boolean active;

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
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Gets the value of the order_number property.
     *
     * @return possible object is {@link Integer}
     */
    public Integer getOrder_number() {
        return order_number;
    }

    /**
     * Sets the value of the order_number property
     *
     * @param order_number allowed object is {@link Integer }
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setOrder_number(Integer order_number) {
        this.order_number = order_number;
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
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setPrice(String price) {
        this.price = price;
        return this;
    }

    /**
     * Gets the value of the voucher property.
     *
     * @return possible object is {@link String}
     */
    public String getVoucher() {
        return voucher;
    }

    /**
     * Sets the value of the voucher property
     *
     * @param voucher allowed object is {@link String }
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setVoucher(String voucher) {
        this.voucher = voucher;
        return this;
    }

    /**
     * Gets the value of the email_sent property.
     *
     * @return possible object is {@link Boolean}
     */
    public Boolean getEmail_sent() {
        return email_sent;
    }

    /**
     * Sets the value of the email_sent property
     *
     * @param email_sent allowed object is {@link Boolean }
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setEmail_sent(Boolean email_sent) {
        this.email_sent = email_sent;
        return this;
    }

    /**
     * Gets the value of the active property.
     *
     * @return possible object is {@link Boolean}
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the value of the active property
     *
     * @param active allowed object is {@link Boolean }
     * @return the {@link VoucherEntity}
     */
    public VoucherEntity setActive(Boolean active) {
        this.active = active;
        return this;
    }
}
