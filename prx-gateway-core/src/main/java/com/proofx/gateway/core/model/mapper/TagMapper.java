package com.proofx.gateway.core.model.mapper;

import com.proofx.gateway.api.v1.model.tag.Tag;
import com.proofx.gateway.core.jpa.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for tags
 *
 * @author ProofX
 * @since 1.0.0
 */
@Mapper(config = ServiceMapperConfiguration.class,
        unmappedSourcePolicy = ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN)
@SuppressWarnings("java:S1610")
public abstract class TagMapper {
    /**
     * model to entity
     *
     * @param model model
     * @return entity
     */
    @Mapping(
            target = "counter", constant = "0"
    )
    public abstract TagEntity toEntity(Tag model);
    /**
     * model to entity
     *
     * @param model model
     * @param entity entity
     */
    public abstract void toEntity(Tag model, @MappingTarget TagEntity entity);
    /**
     * entity to model
     *
     * @param entity entity
     * @return model
     */
    public abstract Tag toModel(TagEntity entity);
}
