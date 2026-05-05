package neurocode.beebetter.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import neurocode.beebetter.model.User;
import neurocode.beebetter.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private OAuthService oAuthService;
    @Autowired private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        String provider = token.getAuthorizedClientRegistrationId();

        OAuthUserInfo userInfo = OAuthUserInfoFactory.extract(provider, oAuth2User);

        User user = oAuthService.processOAuthLogin(userInfo);

        String jwt = jwtService.generateToken(user.getEmail());

        String redirectUrl = "beebetter://auth/callback?token=" + jwt
                + "&userId=" + user.getId()
                + "&name=" + user.getName()
                + "&email=" + user.getEmail();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}