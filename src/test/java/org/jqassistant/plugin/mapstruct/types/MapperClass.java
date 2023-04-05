package org.jqassistant.plugin.mapstruct.types;

import org.mapstruct.Mapper;

@Mapper
public abstract class MapperClass {

    public abstract DTO fromDomain(Entity entity);

    public abstract Entity toDomain(DTO dto);

}
