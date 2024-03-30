package com.junho.systemdesign.urlshortener.repository.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ShortenUrl {

    @Id
    private String id;

}
