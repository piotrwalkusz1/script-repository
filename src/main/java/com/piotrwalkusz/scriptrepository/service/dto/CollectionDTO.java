package com.piotrwalkusz.scriptrepository.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;

/**
 * A DTO for the Collection entity.
 */
public class CollectionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Privacy privacy;

    private Long ownerId;

    private Set<Long> sharedUsers = new HashSet<>();

    private Set<Long> scripts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public Set<Long> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Set<Long> users) {
        this.sharedUsers = users;
    }

    public Set<Long> getScripts() {
        return scripts;
    }

    public void setScripts(Set<Long> scripts) {
        this.scripts = scripts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CollectionDTO collectionDTO = (CollectionDTO) o;
        if(collectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CollectionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", privacy='" + getPrivacy() + "'" +
            "}";
    }


}
