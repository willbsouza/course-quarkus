package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.Follower;
import com.github.quarkussocial.domain.model.Post;
import com.github.quarkussocial.domain.model.User;
import com.github.quarkussocial.domain.repository.FollowerRepository;
import com.github.quarkussocial.domain.repository.PostRepository;
import com.github.quarkussocial.domain.repository.UserRepository;
import com.github.quarkussocial.resources.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private FollowerRepository followerRepository;

    @Inject
    private PostRepository postRepository;

    private Long userId;

    private Long userNotFollowerId;

    private Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp(){
        //user default tests
        User user = new User();
        user.setAge(29);
        user.setName("Will");
        userRepository.persist(user);
        userId = user.getId();

        //create post for user
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        post.setDateTime(LocalDateTime.now());
        postRepository.persist(post);

        //user does not follow
        User userNotFollower = new User();
        userNotFollower.setAge(29);
        userNotFollower.setName("Willams");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //user who follows
        User userFollower = new User();
        userFollower.setAge(29);
        userFollower.setName("Willams");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should create a post for an user.")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var userId = 1L;

        given()
                    .contentType(ContentType.JSON)
                    .body(postRequest)
                    .pathParam("userId", userId)
                .when()
                    .post()
                .then()
                    .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for an inexistent user.")
    public void postForAnInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId = 999L;

        given()
                    .contentType(ContentType.JSON)
                    .body(postRequest)
                    .pathParam("userId", inexistentUserId)
                .when()
                    .post()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when user doesn't exist.")
    public void listPostUserNotFoundTest(){
        var inexistentUserID = 999L;

        given()
                    .pathParam("userId", inexistentUserID)
                .when()
                    .get()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present.")
    public void listPostFollowerHeaderNotSendTest(){
        given()
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .statusCode(400)
                    .body(Matchers.is("You forgot the header followerId."));
    }

    @Test
    @DisplayName("should return 400 when follower doesn't exist.")
    public void listPostFollowerNotFoundTest(){

        var inexistentFollowerId = 999L;

        given()
                    .pathParam("userId", userId)
                    .header("followerId", inexistentFollowerId)
                .when()
                    .get()
                .then()
                    .statusCode(400)
                    .body(Matchers.is("Inexistent followerId."));
    }

    @Test
    @DisplayName("should return 403 when follower isn't a follower.")
    public void listPostNotAFollowerTest(){
        given()
                    .pathParam("userId", userId)
                    .header("followerId", userNotFollowerId)
                .when()
                    .get()
                .then()
                    .statusCode(403)
                    .body(Matchers.is("You are not allowed to view these posts."));
    }

    @Test
    @DisplayName("should return posts.")
    public void listPostTest(){
        given()
                    .pathParam("userId", userId)
                    .header("followerId", userFollowerId)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .body("size()", Matchers.is(1));
    }
}