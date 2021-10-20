package com.mykhailopavliuk.service.impl;

import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.exception.NullEntityReferenceException;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.repository.UrlRepository;
import com.mykhailopavliuk.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Url create(Url url) {
        if (url != null) {
            return urlRepository.save(url);
        }
        throw new NullEntityReferenceException("Url cannot be 'null'");
    }

    @Override
    public Url readById(Long id) {
        return urlRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Url with id " + id + " not found"));
    }

    @Override
    public Url update(Url url) {
        if (url != null) {
            return urlRepository.save(url);
        }
        throw new NullEntityReferenceException("Url cannot be 'null'");
    }

    @Override
    public void deleteById(Long id) {
        urlRepository.deleteById(id);
    }

    @Override
    public List<Url> getAll() {
        return urlRepository.findAll();
    }

    @Override
    public long getAvailableId() {
        return urlRepository.getAvailableId();
    }

    @Override
    public Url readByPath(String path) {
        return urlRepository.findByPath(path).orElseThrow(
                () -> new EntityNotFoundException("Url with path '" + path + "' not found"));
    }
}
