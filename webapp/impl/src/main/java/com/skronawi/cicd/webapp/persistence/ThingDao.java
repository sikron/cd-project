package com.skronawi.cicd.webapp.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ThingDao {

    @Autowired
    private ThingRepo thingRepo;

    public Set<Thing> getAll() {
        return thingRepo.findAll();
    }

    public Thing get(String id) {
        return thingRepo.findOne(id);
    }

    public Thing create(Thing thing) {
        return thingRepo.save(thing);
    }

    public void delete(String id) {
        thingRepo.delete(id);
    }
}
