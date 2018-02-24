package com.piotrwalkusz.scriptrepository.service.mapper;

import com.piotrwalkusz.scriptrepository.domain.*;
import com.piotrwalkusz.scriptrepository.service.dto.CollectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Collection and its DTO CollectionDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CollectionMapper extends EntityMapper<CollectionDTO, Collection> {

    @Mapping(source = "owner.id", target = "ownerId")
    CollectionDTO toDto(Collection collection);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "scripts", ignore = true)
    Collection toEntity(CollectionDTO collectionDTO);

    default Collection fromId(Long id) {
        if (id == null) {
            return null;
        }
        Collection collection = new Collection();
        collection.setId(id);
        return collection;
    }

    default Long fromUserToId(User user) {
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}
