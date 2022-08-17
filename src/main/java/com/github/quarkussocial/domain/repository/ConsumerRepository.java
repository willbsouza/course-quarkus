package com.github.quarkussocial.domain.repository;

import com.github.quarkussocial.domain.model.Consumer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsumerRepository implements PanacheRepository<Consumer> {
}
