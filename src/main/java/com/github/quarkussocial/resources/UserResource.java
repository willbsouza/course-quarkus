package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.User;
import com.github.quarkussocial.domain.repository.UserRepository;
import com.github.quarkussocial.resources.dto.CreateUserRequest;
import com.github.quarkussocial.resources.dto.ResponseError;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository userRepository;
    private Validator validator;
    @Inject
    public UserResource(UserRepository userRepository, Validator validator){
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @GET
    public Response findAll(){
        return Response.ok(userRepository.findAll().list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id){
        User user = userRepository.findById(id);
        if(user != null) {
            return Response.ok(user).build();
        }
        return Response.status(404).build();
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest){

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }

        User user = new User(userRequest);
        userRepository.persist(user);
        return Response.status(201).entity(user).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateById(@PathParam("id") Long id, CreateUserRequest userRequest){
        User user = userRepository.findById(id);
        if(user != null){
            user.setAge(userRequest.getAge());
            user.setName(userRequest.getName());
            return Response.ok(user).build();
        }
        return Response.status(404).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id){
        User user = userRepository.findById(id);
        if(user != null) {
            userRepository.deleteById(id);
            return Response.noContent().build();
        }
        return Response.status(404).build();
    }
}