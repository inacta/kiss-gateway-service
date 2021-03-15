package com.proofx.gateway.core.model.mapper;

import com.proofx.gateway.api.v1.model.humanity.LineItem;
import com.proofx.gateway.api.v1.model.humanity.WebhookRequest;
import com.proofx.gateway.core.jpa.VoucherEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.*;

/**
 * Mapper for vouchers
 *
 * @author ProofX
 * @since 1.0.0
 */
@Mapper(config = ServiceMapperConfiguration.class,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
@SuppressWarnings("java:S1610")
public abstract class VoucherMapper {
    /**
     * model to entity
     *
     * @param model model
     * @param entity entity
     */
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "email_sent", constant = "false")
    public abstract void toEntity(WebhookRequest model, @MappingTarget VoucherEntity entity);
    /**
     * entity to model
     *
     * @param entity entity
     * @return model
     */
    public abstract WebhookRequest toModel(VoucherEntity entity);

    /**
     * model to entity
     *
     * @param model model
     * @param entity entity
     */
    @AfterMapping
    void toEntityAfterMapping(WebhookRequest model, @MappingTarget VoucherEntity entity) {
        entity.setVoucher(RandomStringUtils.randomAlphanumeric(16).toUpperCase().replaceAll("(.{4})", "$1 ").trim());
    }
    /**
     * model to entity
     *
     * @param model model
     * @param entity entity
     */
    public abstract void toEntity(LineItem model, @MappingTarget VoucherEntity entity);
}
