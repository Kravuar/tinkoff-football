package net.kravuar.tinkofffootball.domain.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kravuar.tinkofffootball.domain.model.user.UserInfo;

@Data
@AllArgsConstructor
public class LoggedUser {
    private String accessToken;
    private String refreshToken;
    private UserInfo userInfo;
}
