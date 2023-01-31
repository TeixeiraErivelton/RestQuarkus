package teixeira.erivelton.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teixeira.erivelton.quarkussocial.rest.domain.model.Follower;
import teixeira.erivelton.quarkussocial.rest.domain.model.Post;
import teixeira.erivelton.quarkussocial.rest.domain.model.User;
import teixeira.erivelton.quarkussocial.rest.domain.repository.FollowerRepository;
import teixeira.erivelton.quarkussocial.rest.domain.repository.PostRepository;
import teixeira.erivelton.quarkussocial.rest.domain.repository.UserRepository;
import teixeira.erivelton.quarkussocial.rest.dto.CreatePostRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;

    Long userId;

    Long userNotFollowerId;

    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp(){

        //usuario padrao dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
        userId = user.getId();

        //criada postagem para usuario
        Post post = new Post();
        post.setText("hello");
        post.setUser(user);

        postRepository.persist(post);

        //usuario que nao segue ninguem
        var userNotFollower = new User();
        userNotFollower.setAge(33);
        userNotFollower.setName("Ciclano");

        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //usuario seguidor
        var userFollower = new User();
        userFollower.setAge(33);
        userFollower.setName("Beltrano");

        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);

        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("Should create a post for a user!")
    public void createPostTest(){

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for a inexistent user!")
    public void postForAnInexistentUserTest(){

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", inexistentUserId)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist!")
    public void listPostUserNotFoundTest(){

        var inexistentUserId = 999;

        given()
                .pathParams("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present!")
    public void listPostFollowerHeaderNotSendTest(){

        given()
                .pathParams("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followeId!"));
    }
    @Test
    @DisplayName("Should return 400 when follower doesn't exist!")
    public void listPostFollowerNotFoundTest(){

        var inexistentFollowerId = 999;

        given().
                pathParams("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followeId!"));

    }

    @Test
    @DisplayName("Should return 403 when follower isn't a follower!")
    public void listPostNotAFollowerTest(){

        given().
                pathParams("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see this posts!"));

    }

    @Test
    @DisplayName("Should return posts!")
    public void listPostTest(){

        given().
                pathParams("userId", userId)
                .header("followerId", userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));
    }

}