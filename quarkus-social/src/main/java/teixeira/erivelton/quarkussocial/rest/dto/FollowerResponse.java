package teixeira.erivelton.quarkussocial.rest.dto;

import lombok.Data;
import teixeira.erivelton.quarkussocial.rest.domain.model.Follower;
import teixeira.erivelton.quarkussocial.rest.domain.repository.FollowerRepository;

@Data
public class FollowerResponse {

    private Long id;

    private String name;

    public FollowerResponse() {
    }

    public FollowerResponse(Follower follower){
        this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
