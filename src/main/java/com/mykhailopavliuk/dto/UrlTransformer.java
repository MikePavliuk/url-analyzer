package com.mykhailopavliuk.dto;

import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.service.UrlService;

import java.util.ArrayList;

public class UrlTransformer {
    public static UrlTableRowDTO convertToDto(Url url) {
        return new UrlTableRowDTO(
                url.getId(),
                url.getPath()
        );
    }

    public static Url convertToEntity(UrlTableRowDTO urlDto, User owner) {
        return new Url(
                urlDto.getId(),
                urlDto.getPath(),
                new ArrayList<>(),
                owner
        );
    }
}
