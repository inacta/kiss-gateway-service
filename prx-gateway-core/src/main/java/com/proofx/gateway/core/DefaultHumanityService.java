package com.proofx.gateway.core;

import com.proofx.gateway.api.v1.model.humanity.LineItem;
import com.proofx.gateway.api.v1.model.humanity.WebhookRequest;
import com.proofx.gateway.core.configuration.PropertyService;
import com.proofx.gateway.core.jpa.VoucherEntity;
import com.proofx.gateway.core.model.mapper.VoucherMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    @Inject
    DefaultHumanityEmailService emailService;

    /**
     * Generate a new voucher from webhook
     *
     * @param webhookRequest webhookRequest
     * @return list of generated vouchers
     */
    @Transactional
    public List<String> generateVoucher(WebhookRequest webhookRequest) {
        ArrayList<String> newVouchers = new ArrayList<>();
        for (LineItem item: webhookRequest.getLine_items()) {
            if (item.getProduct_id().equals(propertyService.getVoucherProductId())) {
                VoucherEntity entity = new VoucherEntity();
                this.voucherMapper.toEntity(item, entity);
                this.voucherMapper.toEntity(webhookRequest, entity);
                entity.persistAndFlush();
                newVouchers.add(entity.getVoucher());
            }
        }
        return newVouchers;
    }

}
