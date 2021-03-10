package com.proofx.gateway.core.model.mapper;

import com.proofx.gateway.api.v1.model.Base;
import com.proofx.gateway.core.model.BaseEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

/**
 * Mapper configuration to enable cdi. Furthermore it handels the mapping of all base fields which should never be mapped because they are set from
 * the underlying db implementation.
 *
 * @author inacta AG
 * @since 1.0.0
 */
@MapperConfig(componentModel = "cdi",
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface ServiceMapperConfiguration {

    /**
     * Maps to model
     *
     * @param baseEntity
     *            entity
     * @return model
     */
    Base mapBaseEntity(BaseEntity baseEntity);

    /**
     * maps to entity
     *
     * @param baseDbo
     *            model
     * @return entity
     */
    @Mapping(target = "id",
            ignore = true)
    BaseEntity mapBase(Base baseDbo);
}
