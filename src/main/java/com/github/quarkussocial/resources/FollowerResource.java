package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.User;
import com.github.quarkussocial.domain.model.Follower;
import com.github.quarkussocial.domain.repository.UserRepository;
import com.github.quarkussocial.domain.repository.FollowerRepository;
import com.github.quarkussocial.resources.dto.CreateFollowerRequest;
import com.github.quarkussocial.resources.dto.FollowerResponse;
import com.github.quarkussocial.resources.dto.FollowersPerUserResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository){
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, CreateFollowerRequest createFollowerRequest){
        User user = userRepository.findById(userId);
        User follower = userRepository.findById(createFollowerRequest.getFollowerId());
        if(userId.equals(createFollowerRequest.getFollowerId())){
            return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself.").build();
        }
        if(user == null || follower == null){
            return Response.status(404).build();
        }
        if(!followerRepository.follows(follower, user)){
            followerRepository.persist(new Follower(user, follower));
        }
        return Response.noContent().build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){
        List<Follower> list = followerRepository.findByUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();

        responseObject.setFollowersCount(list.size());
        responseObject.setContent(list.stream().map(FollowerResponse::new).collect(Collectors.toList()));

        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(404).build();
        }
        followerRepository.deleteByFollowerAndUser(followerId, userId);
        return Response.noContent().build();
    }
}
