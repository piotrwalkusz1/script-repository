package com.piotrwalkusz.scriptrepository.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.piotrwalkusz.scriptrepository.domain.enumeration.ScriptLanguage;

/**
 * A DTO for the Script entity.
 */
public class ScriptDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z]*$")
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private ScriptLanguage scriptLanguage;

    @Size(max = 1000000)
    @Lob
    private String code;

    private Integer downloadCount;

    private Long collectionId;

    private Set<TagDTO> tags = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScriptLanguage getScriptLanguage() {
        return scriptLanguage;
    }

    public void setScriptLanguage(ScriptLanguage scriptLanguage) {
        this.scriptLanguage = scriptLanguage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScriptDTO scriptDTO = (ScriptDTO) o;
        if(scriptDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scriptDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScriptDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", scriptLanguage='" + getScriptLanguage() + "'" +
            ", code='" + getCode() + "'" +
            ", downloadCount=" + getDownloadCount() +
            "}";
    }
}
