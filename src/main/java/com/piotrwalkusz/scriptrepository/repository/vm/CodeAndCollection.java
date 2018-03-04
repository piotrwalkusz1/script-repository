package com.piotrwalkusz.scriptrepository.repository.vm;

import com.piotrwalkusz.scriptrepository.domain.Collection;

public class CodeAndCollection {

    public CodeAndCollection(String code, Collection collection) {
        this.code = code;
        this.collection = collection;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    private String code;
    private Collection collection;


}
