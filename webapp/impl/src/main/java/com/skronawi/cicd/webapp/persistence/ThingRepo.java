package com.skronawi.cicd.webapp.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ThingRepo extends CrudRepository<Thing, String> {

    Set<Thing> findAll();
}
