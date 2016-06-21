package com.skronawi.cicd.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.skronawi.cicd.webapp.persistence.Thing;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

public class ThingAppTest {

    private String host;
    private int port;
    private RestTemplate restTemplate;
    private URI uri;
    private ObjectMapper objectMapper;
    private CollectionType thingSetType;

    @BeforeClass
    public void setup() throws Exception {

        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/apptest.properties"));

        host = properties.getProperty("host");
        port = Integer.parseInt(properties.getProperty("port"));

        restTemplate = new RestTemplate();
        //"webapp-impl" is context defined in cargo-deployable, "things" is endpoint of ThingResource
        uri = URI.create("http://" + host + ":" + port + "/webapp-impl/things");

        objectMapper = new ObjectMapper();
        thingSetType = objectMapper.getTypeFactory().constructCollectionType(Set.class, Thing.class);
    }

    @Test
    public void createGetDelete() throws Exception {

        //get existing dummy things
        ResponseEntity<Set<Thing>> existing = restTemplate.exchange(uri,
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Set<Thing>>() {
                });
        Assert.assertEquals(existing.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(existing.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        Assert.assertEquals(existing.getBody().size(), 2);

        //create a new thing
        Thing thing = new Thing();
        thing.setValue("c");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String thingJson = objectMapper.writeValueAsString(thing);
        HttpEntity<String> entity = new HttpEntity<String>(thingJson, headers);
        ResponseEntity<Thing> created = restTemplate.postForEntity(uri, entity, Thing.class);
        Assert.assertEquals(created.getStatusCode(), HttpStatus.CREATED);
        Assert.assertTrue(created.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(created.getBody().getId());
        Assert.assertEquals(created.getBody().getValue(), thing.getValue());

        //explicitly get the thing
        ResponseEntity<Thing> retrieved = restTemplate.exchange(uri + "/" + created.getBody().getId(),
                HttpMethod.GET, HttpEntity.EMPTY, Thing.class);
        Assert.assertEquals(retrieved.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(retrieved.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        Assert.assertEquals(retrieved.getBody().getValue(), thing.getValue());
        Assert.assertNotNull(retrieved.getBody().getId());

        //get existing dummy things incl new thing
        ResponseEntity<Set<Thing>> current = restTemplate.exchange(uri,
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Set<Thing>>() {
                });
        Assert.assertEquals(current.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(current.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        Assert.assertEquals(current.getBody().size(), 3);

        //delete thing
        ResponseEntity<String> response = restTemplate.exchange(uri + "/" + created.getBody().getId(),
                HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);

        //get existing dummy things without deleted thing
        existing = restTemplate.exchange(uri,
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Set<Thing>>() {
                });
        Assert.assertEquals(existing.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(existing.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        Assert.assertEquals(existing.getBody().size(), 2);
    }
}
