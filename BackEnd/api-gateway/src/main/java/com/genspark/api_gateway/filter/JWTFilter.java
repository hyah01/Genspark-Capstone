package com.genspark.api_gateway.filter;


import com.genspark.api_gateway.util.APIJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JWTFilter extends AbstractGatewayFilterFactory<JWTFilter.Config> {
    @Autowired
    private RouteValidator validator;

    @Autowired
    private APIJwtUtil jwtUtil;
    public JWTFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())){
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer")){
                    authHeader = authHeader.substring(7);
                }
                try {
//                    template.getForObject("http://USER-SERVICE//validate?token" + authHeader, String.class);

                    jwtUtil.tokenValidate(authHeader);
                } catch (Exception e) {
                    System.out.println("Unauthorized Access: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

}
