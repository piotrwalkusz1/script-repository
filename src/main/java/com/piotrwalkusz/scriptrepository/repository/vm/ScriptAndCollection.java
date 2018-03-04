package com.piotrwalkusz.scriptrepository.repository.vm;

import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.domain.Script;

public class ScriptAndCollection {

    public ScriptAndCollection(Script script, Collection collection) {
        this.script = script;
        this.collection = collection;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public String getCode() {
        return script.getCode();
    }

    public void setCode(String code) {
        script.setCode(code);
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    private Script script;
    private Collection collection;


}
