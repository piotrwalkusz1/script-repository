package com.piotrwalkusz.scriptrepository.service.mapper;

import com.piotrwalkusz.scriptrepository.domain.*;
import com.piotrwalkusz.scriptrepository.service.dto.ScriptDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Script and its DTO ScriptDTO.
 */
@Mapper(componentModel = "spring", uses = {CollectionMapper.class, TagMapper.class})
public interface ScriptMapper extends EntityMapper<ScriptDTO, Script> {

    @Mapping(source = "collection.id", target = "collectionId")
    ScriptDTO toDto(Script script);

    @Mapping(source = "collectionId", target = "collection")
    Script toEntity(ScriptDTO scriptDTO);

    default Script fromId(Long id) {
        if (id == null) {
            return null;
        }
        Script script = new Script();
        script.setId(id);
        return script;
    }
}
