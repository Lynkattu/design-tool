package lynkattu.example.designer_tool.authentication;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // contains http request
            @NonNull HttpServletResponse response, // http response
            @NonNull FilterChain filterChain) // tell when this filter is done and ready to move on
            throws ServletException, IOException, java.io.IOException {

        String header = request.getHeader("Authorization");

        // check authorization header
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract JWT token from header
        String token = header.substring(7);

        // try to read JWT token
        try {
            String username = jwtService.extractUsername(token);

            //check if username exists in token and is this request already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // check if JWT token is valid
                if (jwtService.isValid(token, userDetails)) {

                    // create authentication object for spring security
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException e) {
            // Invalid/expired JWT
        }

        filterChain.doFilter(request, response);
    }
}

