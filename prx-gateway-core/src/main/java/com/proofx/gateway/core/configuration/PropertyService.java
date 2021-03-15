package com.proofx.gateway.core.configuration;

import com.proofx.gateway.api.v1.model.ServiceRuntimeException;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.IllegalProductException;
import javax.inject.Inject;
import javax.ws.rs.core.Context;

import static com.proofx.gateway.api.v1.model.ErrorResponseMessage.PROPERTY_NOT_FOUND_ERROR;
import static java.util.stream.StreamSupport.stream;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

/**
 * The default system-property provider for configuration
 *
 * A property key has the following pattern:
 *
 * TENANT_ID.PROPERTY_NAME
 *
 * @author inacta AG
 * @since 1.0.0
 */
@RequestScoped
public class PropertyService {

    private static final String APPLICATION_KEY = "PROOFX";
    private static final String TEZOS_PROVIDER = "TEZOS_PROVIDER";
    private static final String MNEMONIC = "MNEMONIC";
    private static final String PASSPHRASE = "PASSPHRASE";
    private static final String VOUCHER_PRODUCT_ID = "VOUCHER_PRODUCT_ID";
    private static final String SHOPIFY_KEY = "SHOPIFY_KEY";
    private static final String MJ_APIKEY_PUBLIC = "MJ_APIKEY_PUBLIC";
    private static final String MJ_APIKEY_PRIVATE = "MJ_APIKEY_PRIVATE";
    private static final String MJ_SENDER_EMAIL = "MJ_SENDER_EMAIL";
    private static final String ENVIRONMENT = "ENVIRONMENT";
    private static final String VOUCHER_URI = "VOUCHER_URI";

    private final String tenant;

    @Inject
    PropertyService(@Context final RoutingContext routingContext) {
        if (isHttpRequest(routingContext)) {
            this.tenant = routingContext.request().headers().contains("tenant") ? routingContext.request().getHeader("tenant") : "dev";
        } else {
            this.tenant = "dev";
        }
    }

    /**
     * Gets the application-key is need e.g. to call the isp-s3-storage service.
     *
     * @return possible object is {@link String}
     */
    public String getApplicationKey() {

        return APPLICATION_KEY;
    }

    /**
     * Gets the value of the tenant property. If a forced tenant is set, the forced tenant will be returned instead.
     *
     * @return possible object is {@link String}
     */
    public String getTenant() {

        return this.tenant;
    }

    /**
     * A method for getting a configuration-property
     *
     * @param key the of the configuration entry
     * @return the value of the configuration entry
     */
    public String getProperty(final String key) {

        final String tenantPropertyKey = this.tenant.toUpperCase() + "_" + key.toUpperCase();
        if (stream(getConfig().getPropertyNames().spliterator(), false).anyMatch(tenantPropertyKey::equals)) {
            return getConfig().getValue(tenantPropertyKey, String.class);
        } else if (stream(getConfig().getPropertyNames().spliterator(), false).anyMatch(key::equals)) {
            return getConfig().getValue(key, String.class);
        }

        throw new ServiceRuntimeException(PROPERTY_NOT_FOUND_ERROR, key, this.tenant);
    }

    private boolean isHttpRequest(RoutingContext routingContext) {
        try {
            routingContext.request();
            return true;
        } catch (IllegalProductException e) {
            return false;
        }
    }

    public String getTezosProvider() {
        return this.getProperty(TEZOS_PROVIDER);
    }
    public String getMnemonic() {
        return this.getProperty(MNEMONIC);
    }
    public String getPassphrase() {
        return this.getProperty(PASSPHRASE);
    }
    public Long getVoucherProductId() { return Long.parseLong(this.getProperty(VOUCHER_PRODUCT_ID)); }
    public String getShopifyKey() { return this.getProperty(SHOPIFY_KEY); }
    public String getMjApikeyPublic() { return this.getProperty(MJ_APIKEY_PUBLIC); }
    public String getMjApikeyPrivate() { return this.getProperty(MJ_APIKEY_PRIVATE); }
    public String getSenderEmail() { return this.getProperty(MJ_SENDER_EMAIL); }
    public boolean isProduction() {
        return !this.getProperty(ENVIRONMENT).equals("dev");
    }
    public String getVoucherUri() {
        return this.getProperty(VOUCHER_URI);
    }
}
