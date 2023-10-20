package net.kravuar.tinkofffootball.domain.model.dto;

import lombok.Getter;
import net.kravuar.tinkofffootball.domain.model.user.User;

@Getter
public class DetailedUserInfoDTO extends UserInfoDTO {
//    todo: Teams, statistics ...

    public DetailedUserInfoDTO(User user) {
        super(user);
    }
}
