package com.piotrwalkusz.scriptrepository.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;

/**
 * A Collection.
 */
@Entity
@Table(name = "collection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "collection")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy", nullable = false)
    private Privacy privacy;

    @ManyToOne(optional = false)
    @NotNull
    private User owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "collection_shared_users",
               joinColumns = @JoinColumn(name="collections_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="shared_users_id", referencedColumnName="id"))
    private Set<User> sharedUsers = new HashSet<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Script> scripts = new HashSet<>();

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

    public Collection name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public Collection privacy(Privacy privacy) {
        this.privacy = privacy;
        return this;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public User getOwner() {
        return owner;
    }

    public Collection owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<User> getSharedUsers() {
        return sharedUsers;
    }

    public Collection sharedUsers(Set<User> users) {
        this.sharedUsers = users;
        return this;
    }

    public Collection addSharedUsers(User user) {
        this.sharedUsers.add(user);
        user.getSharedCollections().add(this);
        return this;
    }

    public Collection removeSharedUsers(User user) {
        this.sharedUsers.remove(user);
        user.getSharedCollections().remove(this);
        return this;
    }

    public void setSharedUsers(Set<User> users) {
        this.sharedUsers = users;
    }

    public Set<Script> getScripts() {
        return scripts;
    }

    public Collection scripts(Set<Script> scripts) {
        this.scripts = scripts;
        return this;
    }

    public Collection addScripts(Script script) {
        this.scripts.add(script);
        script.setCollection(this);
        return this;
    }

    public Collection removeScripts(Script script) {
        this.scripts.remove(script);
        script.setCollection(null);
        return this;
    }

    public void setScripts(Set<Script> scripts) {
        this.scripts = scripts;
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
        Collection collection = (Collection) o;
        if (collection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collection{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", privacy='" + getPrivacy() + "'" +
            "}";
    }
}
