package io.github.shiryu.autosell.util;

import java.util.List;
import java.util.stream.Collectors;

public class ReplaceAllList {

    private List<String> list;

    public ReplaceAllList(List<String> list){
        this.list = list;
    }

    public ReplaceAllList replaceAll(String regex, String replacement){
        this.list = list.stream().map(l -> l.replaceAll(regex, replacement)).collect(Collectors.toList());

        return this;
    }

    public List<String> value(){
        return this.list;
    }
}
