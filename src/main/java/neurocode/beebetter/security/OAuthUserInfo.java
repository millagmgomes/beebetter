package neurocode.beebetter.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfo {

    private String name;    // ← name primeiro
    private String email;   // ← email segundo
    private String provider;
}