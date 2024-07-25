package org.jqassistant.plugin.mapstruct.types;

import org.mapstruct.Mapper;

@Mapper
public interface MapperInterface {

    DTO fromDomain(Entity entity);

    Entity toDomain(DTO dto);


}
