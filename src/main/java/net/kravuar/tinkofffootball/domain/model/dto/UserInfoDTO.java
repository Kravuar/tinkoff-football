package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.user.User;

@Data
public class UserInfoDTO {
    private String username;
//    TODO: profile pic

    public UserInfoDTO(User user) {
        this.username = user.getUsername();
    }
}
