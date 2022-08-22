package com.github.quarkussocial.resources;

import com.github.quarkussocial.domain.model.Follower;
import com.github.quarkussocial.domain.model.User;
import com.github.quarkussocial.domain.repository.FollowerRepository;
import com.github.quarkussocial.domain.repository.UserRepository;
import com.github.quarkussocial.resources.dto.CreateFollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private FollowerRepository followerRepository;

    private Long userId;
    private Long followerId;

    @BeforeEach
    @Transactional
    public void setUp() {
        //user default tests
        User user = new User();
        user.setAge(29);
        user.setName("Will");
        userRepository.persist(user);
        userId = user.getId();

        //follower
        User follower = new User();
        follower.setAge(29);
        follower.setName("Willams");
        userRepository.persist(follower);
        followerId = follower.getId();

        //create a follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when followerId is equal to User id")
    public void sameUserAsFollowerTest(){
        var body = new CreateFollowerRequest();
        body.setFollowerId(userId);

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", userId)
                .when()
                    .put()
                .then()
                    .statusCode(409)
                .body(Matchers.is("You can't follow yourself."));
    }

    @Test
    @DisplayName("should return 404 on follow a user when User id doesn't exist")
    public void userNotFoundWhenTryingToFollowTest(){

        var body = new CreateFollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 999L;

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", inexistentUserId)
                .when()
                    .put()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest(){

        var body = new CreateFollowerRequest();
        body.setFollowerId(followerId);

        given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .pathParam("userId", userId)
                .when()
                    .put()
                .then()
                    .statusCode(204);
    }

    @Test
    @DisplayName("should return 404 on list user followers and User id doesn't exist")
    public void userNotFoundWhenListingFollowersTest(){

        var inexistentUserId = 999L;

        given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", inexistentUserId)
                .when()
                    .get()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should list a user's followers")
    public void listFollowersTest(){
        var response =
        given()
                    .contentType(ContentType.JSON)
                    .pathParam("userId", userId)
                .when()
                    .get()
                .then()
                    .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");
        assertEquals(200, response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());
    }

    @Test
    @DisplayName("should return 404 on unfollow user and User id doesn't exist")
    public void userNotFoundWhenWhenUnfollowingAnUserTest(){

        var inexistentUserId = 999L;

        given()
                    .pathParam("userId", inexistentUserId)
                    .queryParam("followerId", followerId)
                .when()
                    .delete()
                .then()
                    .statusCode(404);
    }

    @Test
    @DisplayName("should unfollow an user and User id doesn't exist")
    public void unfollowUserTest(){
         given()
                    .pathParam("userId", userId)
                    .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(204);
    }
}