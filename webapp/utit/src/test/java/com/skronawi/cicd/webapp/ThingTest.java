package com.skronawi.cicd.webapp;

import com.skronawi.cicd.webapp.persistence.Thing;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ThingTest {

    @Test
    public void test(){
        Thing thing = new Thing();
        thing.setValue("12");
        thing.setId("id");
        Assert.assertEquals(thing.getId(), "id");
        Assert.assertEquals(thing.getValue(), "12");
    }
}
