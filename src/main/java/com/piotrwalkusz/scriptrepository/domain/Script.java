package com.piotrwalkusz.scriptrepository.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.piotrwalkusz.scriptrepository.domain.enumeration.ScriptLanguage;

/**
 * A Script.
 */
@Entity
@Table(name = "script")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "script")
public class Script implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z]*$")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "script_language", nullable = false)
    private ScriptLanguage scriptLanguage;

    @Size(max = 1000000)
    @Lob
    @Column(name = "code")
    private String code;

    @Column(name = "download_count")
    private Integer downloadCount;

    @ManyToOne(optional = false)
    @NotNull
    private Collection collection;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "script_tags",
               joinColumns = @JoinColumn(name="scripts_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Script name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Script description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScriptLanguage getScriptLanguage() {
        return scriptLanguage;
    }

    public Script scriptLanguage(ScriptLanguage scriptLanguage) {
        this.scriptLanguage = scriptLanguage;
        return this;
    }

    public void setScriptLanguage(ScriptLanguage scriptLanguage) {
        this.scriptLanguage = scriptLanguage;
    }

    public String getCode() {
        return code;
    }

    public Script code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public Script downloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
        return this;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Collection getCollection() {
        return collection;
    }

    public Script collection(Collection collection) {
        this.collection = collection;
        return this;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Script tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Script addTags(Tag tag) {
        this.tags.add(tag);
        tag.getScripts().add(this);
        return this;
    }

    public Script removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getScripts().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Script script = (Script) o;
        if (script.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), script.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Script{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", scriptLanguage='" + getScriptLanguage() + "'" +
            ", code='" + getCode() + "'" +
            ", downloadCount=" + getDownloadCount() +
            "}";
    }
}
