package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.user.User;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
//    TODO: profile pic

    public UserInfoDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UserInfoDTO(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.username = userInfo.getUsername();
    }
}
