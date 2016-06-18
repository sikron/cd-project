package com.skronawi.cicd.webapp.web;

import com.skronawi.cicd.webapp.persistence.Thing;
import com.skronawi.cicd.webapp.persistence.ThingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Set;

@RestController
@RequestMapping(value = "/things")
public class ThingResource {

    @Autowired
    private ThingDao thingDao;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<Thing> getAll() {
        return thingDao.getAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Thing getById(@PathVariable("id") String id) {
        return thingDao.get(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Thing create(@RequestBody Thing thing) {
        return thingDao.create(thing);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        thingDao.delete(id);
    }

    //TODO just for manual testing
    @PostConstruct
    public void insertDummies() {
        Thing thing = new Thing();
        thing.setValue("42");
        thingDao.create(thing);
        thing = new Thing();
        thing.setValue("qwer");
        thingDao.create(thing);
    }
}
