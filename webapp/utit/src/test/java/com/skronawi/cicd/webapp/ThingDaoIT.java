package com.skronawi.cicd.webapp;

import com.skronawi.cicd.webapp.persistence.PersistenceConfig;
import com.skronawi.cicd.webapp.persistence.Thing;
import com.skronawi.cicd.webapp.persistence.ThingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@ContextConfiguration(classes = PersistenceConfig.class)
public class ThingDaoIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private ThingDao thingDao;

    @Test
    public void createGetDelete() throws Exception{

        Set<Thing> all = thingDao.getAll();
        Assert.assertTrue(all.isEmpty());

        Thing thing = new Thing();
        thing.setValue("v");

        Thing created = thingDao.create(thing);
        Assert.assertNotNull(created);
        Assert.assertEquals(created.getValue(), thing.getValue());
        Assert.assertNotNull(created.getId());

        Thing retrieved = thingDao.get(created.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(retrieved.getValue(), thing.getValue());

        thingDao.delete(retrieved.getId());
        Thing deleted = thingDao.get(retrieved.getId());
        Assert.assertNull(deleted);
    }
}
