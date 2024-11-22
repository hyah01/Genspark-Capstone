package com.genspark.api_gateway.filter;


import com.genspark.api_gateway.util.APIJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
            ServerHttpRequest request = exchange.getRequest();

            // Bypass JWT validation for OPTIONS requests
            if (request.getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }
            if (validator.isSecured.test(exchange.getRequest())) {
                // Header contains token or not
                System.out.println(exchange.getRequest().getHeaders());
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

                    System.out.println("Token not found");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                String token = null;
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }

                try {
                    // Validate JWT Token
                    jwtUtil.tokenValidate(token);

//                    // Extract roles from JWT Token
//                    List<String> roles = jwtUtil.extractRoles(authHeader);
//
//                    // Validate if the user has the required role(s)
//                    if (!hasRequiredRole(exchange.getRequest().getURI().getPath(), roles)) {
//                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                        return exchange.getResponse().setComplete();
//                    }
//                    System.out.println(roles);

                    // Forward the Authorization header to the downstream service
                    request = exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, authHeader)
                            .build();

                } catch (Exception e) {
                    System.out.println("Unauthorized Access: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange.mutate().request(request).build());
        }));
    }
    private boolean hasRequiredRole(String path, List<String> roles) {
        // Implement your logic to determine if the roles match the required roles for the specific path
        // This could be a mapping of paths to roles, or some other logic to check against the roles.
        // For example:
        if (path.startsWith("/users/admin") && roles.contains("ADMIN")) {
            return true;
        }
        if (path.startsWith("/users/user") && roles.contains("USER")) {
            return true;
        }
        if (path.startsWith("/users/adminuser") && (roles.contains("USER") || roles.contains("ADMIN"))) {
            return true;
        }
        if (path.startsWith("/cart") && (roles.contains("USER") || roles.contains("ADMIN"))) {
            return true;
        }
        // Add more role checks as necessary
        return false;
    }

    public static class Config{

    }

}
