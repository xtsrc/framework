package com.xt.framework.auth.config;

import com.xt.framework.auth.service.JwtTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author tao.xiong
 * @Description 授权服务器
 * @Date 2022/7/15 15:12
 */

@Configuration
@EnableAuthorizationServer
public class Auth2Config extends AuthorizationServerConfigurerAdapter {

    @Resource
    public PasswordEncoder passwordEncoder;

    @Resource
    public UserDetailsService kiteUserDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenStore jwtTokenStore;

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Resource
    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {

        //jwt 增强模式
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancerList);
        endpoints.tokenStore(jwtTokenStore)
                .userDetailsService(kiteUserDetailsService)
                //支持 password 模式
                .authenticationManager(authenticationManager)
                .tokenEnhancer(enhancerChain)
                .accessTokenConverter(jwtAccessTokenConverter);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //请求端定义的 client-id 和 client-secret
                .withClient("framework-producer")
                .secret(passwordEncoder.encode("secret-8888"))
                .redirectUris("http://dev.xtzoo.com:8010/callback")
                //授权类型：authorization_code：授权码类型。implicit：隐式授权类型。password：资源所有者（即用户）密码类型。
                // client_credentials：客户端凭据（客户端ID以及Key）类型。refresh_token：通过以上授权获得的刷新令牌来获取新的令牌
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                //token 的有效期
                .accessTokenValiditySeconds(3600)
                .autoApprove(false)
                //限制客户端访问的权限 在限定范围内换取token
                .scopes("all")
                .and()
                .withClient("user-client")
                .secret(passwordEncoder.encode("secret-8888"))
                .redirectUris("http://dev.xtzoo.com:8020/callback")
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .autoApprove(true)
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //允许客户端访问 OAuth2 授权接口 否则请求 token 会返回 401
        security.allowFormAuthenticationForClients();
        //允许已授权用户访问 checkToken 接口
        security.checkTokenAccess("isAuthenticated()");
        //允许已授权用户访问 获取 token 接口
        security.tokenKeyAccess("isAuthenticated()");
    }


    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                String grantType = authentication.getOAuth2Request().getGrantType();
                //授权码和密码模式才自定义token信息
                if ("authorization_code".equals(grantType) || "grant_type_password".equals(grantType)) {
                    String userName = authentication.getUserAuthentication().getName();
                    // 自定义一些token 信息
                    Map<String, Object> additionalInformation = new HashMap<>(16);
                    additionalInformation.put("user_name", userName);
                    additionalInformation = Collections.unmodifiableMap(additionalInformation);
                    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                }
                return super.enhance(accessToken, authentication);
            }
        };
        // 设置签署key
        converter.setSigningKey("testKey");
        return converter;
    }

    @Bean
    public TokenStore jwtTokenStore() {
        //基于jwt实现令牌（Access Token）保存
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}

