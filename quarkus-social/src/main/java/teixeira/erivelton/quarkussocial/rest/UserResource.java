package teixeira.erivelton.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
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

        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }
    
    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
        User user = User.findById(id);
        if (user != null){
            user.delete();
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData){

        User user = User.findById(id);
        if (user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return  Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
}
