package com.mykhailopavliuk.dto;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class UrlTableRowDTO extends RecursiveTreeObject<UrlTableRowDTO> {
    private long id;
    private String path;

    public UrlTableRowDTO(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
