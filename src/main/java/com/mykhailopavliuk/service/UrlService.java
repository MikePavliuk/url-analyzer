package com.mykhailopavliuk.service;

import com.mykhailopavliuk.model.Url;

public interface UrlService extends CrudService<Url, Long> {
    Url readByPath(String path);
}
