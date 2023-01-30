package teixeira.erivelton.quarkussocial.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FollowerPerUserResponse {

    private Integer followersCount;

    private List<FollowerResponse> content;
}
