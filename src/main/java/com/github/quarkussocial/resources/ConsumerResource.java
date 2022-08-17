package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.Consumer;
import com.github.quarkussocial.domain.repository.ConsumerRepository;
import com.github.quarkussocial.resources.dto.CreateConsumerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/consumers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConsumerResource {

    private ConsumerRepository consumerRepository;
    @Inject
    public ConsumerResource(ConsumerRepository consumerRepository){
        this.consumerRepository = consumerRepository;
    }

    @GET
    public Response findAll(){
        return Response.ok(consumerRepository.findAll().list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id){
        Consumer consumer = consumerRepository.findById(id);
        if(consumer != null) {
            return Response.ok(consumer).build();
        }
        return Response.status(404).build();
    }

    @POST
    @Transactional
    public Response createConsumer(CreateConsumerRequest consumerRequest){
        Consumer consumer = new Consumer(consumerRequest);
        consumerRepository.persist(consumer);
        return Response.ok(consumer).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateById(@PathParam("id") Long id, CreateConsumerRequest consumerRequest){
        Consumer consumer = consumerRepository.findById(id);
        if(consumer != null){
            consumer.setAge(consumerRequest.getAge());
            consumer.setName(consumerRequest.getName());
            return Response.ok(consumer).build();
        }
        return Response.status(404).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id){
        Consumer consumer = consumerRepository.findById(id);
        if(consumer != null) {
            consumerRepository.deleteById(id);
            return Response.noContent().build();
        }
        return Response.status(404).build();
    }
}