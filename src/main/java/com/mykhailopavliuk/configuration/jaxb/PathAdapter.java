package com.mykhailopavliuk.configuration.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathAdapter extends XmlAdapter<String, Path> {
    @Override
    public Path unmarshal(String v) {
        if (v == null || v.isEmpty()) {
            return null;
        }
        return Paths.get(v);

    }

    @Override
    public String marshal(Path v) {
        if (v==null) return null;
        return v.toString();
    }
}
