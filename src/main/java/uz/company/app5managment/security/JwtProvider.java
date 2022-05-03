package uz.company.app5managment.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.company.app5managment.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private final String secret = "MyNameIsAlisherAndIBornInUzbekistan";
    private final long expireTime = 36000_000; //10 hours

    public String generateToken(String username, Set<Role> roles) {

        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return token;
    }

    public String getUsernameFromToken(String token) {

        try {
            String username = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return username;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
