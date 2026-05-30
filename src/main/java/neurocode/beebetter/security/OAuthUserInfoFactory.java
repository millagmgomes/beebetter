package neurocode.beebetter.security;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuthUserInfoFactory {

    public static OAuthUserInfo extract(String provider, OAuth2User oAuth2User) {
        return switch (provider.toLowerCase()) {
            case "google" -> new OAuthUserInfo(
                    oAuth2User.getAttribute("name"),
                    oAuth2User.getAttribute("email"),
                    "google"
            );
            case "facebook" -> new OAuthUserInfo(
                    oAuth2User.getAttribute("name"),
                    oAuth2User.getAttribute("email"),
                    "facebook"
            );
            case "twitter" -> {
                var data = (java.util.Map<?, ?>) oAuth2User.getAttribute("data");
                yield new OAuthUserInfo(
                        data != null ? (String) data.get("name") : "Twitter User",
                        data != null ? (String) data.get("email") : null,
                        "twitter"
                );
            }
            default -> throw new RuntimeException("Provedor não suportado: " + provider);
        };
    }
}