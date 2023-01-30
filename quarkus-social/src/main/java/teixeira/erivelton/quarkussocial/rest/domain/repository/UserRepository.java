package teixeira.erivelton.quarkussocial.rest.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import teixeira.erivelton.quarkussocial.rest.domain.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {


}
