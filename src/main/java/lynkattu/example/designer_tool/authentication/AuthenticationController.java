package lynkattu.example.designer_tool.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}

