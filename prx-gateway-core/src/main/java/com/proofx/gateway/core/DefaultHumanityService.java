package com.proofx.gateway.core;

import com.proofx.gateway.api.v1.model.humanity.LineItem;
import com.proofx.gateway.api.v1.model.humanity.WebhookRequest;
import com.proofx.gateway.core.configuration.PropertyService;
import com.proofx.gateway.core.model.VoucherEntity;
import com.proofx.gateway.core.model.mapper.VoucherMapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * The default implementation of the REST endpoint for the humanity service
 *
 * @author ProofX
 * @since 1.0.0
 */
@RequestScoped
public class DefaultHumanityService {

    @Inject
    PropertyService propertyService;

    @Inject
    VoucherMapper voucherMapper;

    Long productId;

    @PostConstruct
    void init() {
        this.productId = propertyService.getVoucherProductId();
    }


    /**
     * Generate a new voucher from webhook
     *
     * @param webhookRequest webhookRequest
     */
    @Transactional
    public void generateVoucher(WebhookRequest webhookRequest) {
        for (LineItem item: webhookRequest.getLine_items()) {
            if (item.getProduct_id().equals(this.productId)) {
                VoucherEntity entity = new VoucherEntity();
                this.voucherMapper.toEntity(item, entity);
                this.voucherMapper.toEntity(webhookRequest, entity);
                entity.persistAndFlush();
            }
        }
    }

}
