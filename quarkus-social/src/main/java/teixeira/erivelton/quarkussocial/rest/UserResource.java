package teixeira.erivelton.quarkussocial.rest;

import teixeira.erivelton.quarkussocial.rest.domain.model.User;
import teixeira.erivelton.quarkussocial.rest.dto.CreateUserRequest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUsers(CreateUserRequest userRequest){

        User user = new User();

        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){

        return Response.ok().build();
    }
}
