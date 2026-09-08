package lynkattu.example.designer_tool.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // generate cryptographic key that JWT can use to sign and verify tokens
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }

    // generate JWT token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // puts the username into the JWT's sub
                .issuedAt(new Date()) // JWT creation time (iat in JWT)
                .expiration(new Date(System.currentTimeMillis() + expiration)) // token expiration time
                .signWith(getKey()) // sign cryptographically JWT token (getKey method from this class)
                .compact(); // convert JWT builder to string format
    }

    // determinate owner of token for spring
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) // verify JWT signature
                .build()
                .parseSignedClaims(token) // parse signed JWT
                .getPayload() // get JWT claims
                .getSubject(); // get user
    }

    // check if user is correct and check if token have expired
    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isExpired(token);
    }

    // check is token expired
    private boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

}
