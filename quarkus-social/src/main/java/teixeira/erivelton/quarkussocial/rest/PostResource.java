package teixeira.erivelton.quarkussocial.rest;

import io.quarkus.panache.common.Sort;
import teixeira.erivelton.quarkussocial.rest.domain.model.Post;
import teixeira.erivelton.quarkussocial.rest.domain.model.User;
import teixeira.erivelton.quarkussocial.rest.domain.repository.FollowerRepository;
import teixeira.erivelton.quarkussocial.rest.domain.repository.PostRepository;
import teixeira.erivelton.quarkussocial.rest.domain.repository.UserRepository;
import teixeira.erivelton.quarkussocial.rest.dto.CreatePostRequest;
import teixeira.erivelton.quarkussocial.rest.dto.PostResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository, FollowerRepository followerRepository) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {

        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();

        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(
            @PathParam("userId") Long userId,
            @HeaderParam("followerId") Long followerId) {

        User user = userRepository.findById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (followerId == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("You forgot the header followeId!").build();
        }

        User follower = userRepository.findById(followerId);

        if (follower == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Inexistent followeId!").build();
        }

        boolean follows = followerRepository.follows(follower, user);

        if (!follows){

            return Response.status(Response.Status.FORBIDDEN).entity("You can't see this posts!").build();
        }


        var query = postRepository.find("user",
                Sort.by("dateTime", Sort.Direction.Descending), user);

        var list = query.list();

//        var postResponseList = list.stream().map(post -> PostResponse.fromEntity(post)).collect(Collectors.toList());
        var postResponseList = list.stream().map(PostResponse::fromEntity).collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
