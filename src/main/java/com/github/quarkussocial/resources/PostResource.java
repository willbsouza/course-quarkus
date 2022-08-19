package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.Consumer;
import com.github.quarkussocial.domain.model.Post;
import com.github.quarkussocial.domain.repository.ConsumerRepository;
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

@Path("/consumers/{consumerId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private ConsumerRepository consumerRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(ConsumerRepository consumerRepository, PostRepository postRepository){
        this.consumerRepository = consumerRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response createPost(@PathParam("consumerId") Long consumerId, CreatePostRequest createPostRequest) {
        Consumer consumer = consumerRepository.findById(consumerId);
        if(consumer == null) {
            return Response.status(404).build();
        }
        Post post = new Post(createPostRequest.getText(), LocalDateTime.now(), consumer);
        postRepository.persist(post);
        return Response.status(201).entity(post).build();
    }

    @GET
    public Response listPosts(@PathParam("consumerId") Long consumerId){
        Consumer consumer = consumerRepository.findById(consumerId);
        if(consumer == null) {
            return Response.status(404).build();
        }
        List<PostResponse> postsResponseList = postRepository.list("consumer", Sort.by("datetime", Sort.Direction.Descending), consumer)
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return Response.ok(postsResponseList).build();
    }
}
