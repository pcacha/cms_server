package cz.zcu.students.cacha.cms_server.security;

import cz.zcu.students.cacha.cms_server.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.cms_server.security.SecurityConstants.EXPIRATION_TIME;
import static cz.zcu.students.cacha.cms_server.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + EXPIRATION_TIME);
        String userId = Long.toString(user.getId());
        String createdAt = Long.toString(user.getCreatedAt().getTime());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("createdAt", createdAt);
        claims.put("isAuthor", user.isAuthor());
        claims.put("isReviewer", user.isReviewer());
        claims.put("isAdmin", user.isAdmin());

        System.out.println("Expire: " + expireDate.getTime());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }
        catch(SignatureException ex) {
            System.out.println("Invalid JWT Signature");
        }
        catch(MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        }
        catch(ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        }
        catch(UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        }
        catch(IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }

        return false;
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return Long.parseLong((String) claims.get("id"));
    }
}
