package neurocode.beebetter.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfo {
    private String name;
    private String email;
    private String provider;
}