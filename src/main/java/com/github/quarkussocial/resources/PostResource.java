package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.User;
import com.github.quarkussocial.domain.model.Post;
import com.github.quarkussocial.domain.repository.FollowerRepository;
import com.github.quarkussocial.domain.repository.UserRepository;
import com.github.quarkussocial.domain.repository.PostRepository;
import com.github.quarkussocial.resources.dto.CreatePostRequest;
import com.github.quarkussocial.resources.dto.PostResponse;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response createPost(@PathParam("userId") Long userId, CreatePostRequest createPostRequest) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(404).build();
        }
        Post post = new Post(createPostRequest.getText(), LocalDateTime.now(), user);
        postRepository.persist(post);
        return Response.status(201).entity(post).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(404).build();
        }
        if(followerId == null){
            return Response.status(400).entity("You forgot the header followerId.").build();
        }

        User follower = userRepository.findById(followerId);

        if(follower == null){
            return Response.status(400).entity("Inexistent followerId.").build();
        }
        if (!followerRepository.follows(follower, user)){
            return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to view these posts.").build();
        }

        List<PostResponse> postsResponseList = postRepository.list("user", Sort.by("datetime", Sort.Direction.Descending), user)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return Response.ok(postsResponseList).build();
    }
}
