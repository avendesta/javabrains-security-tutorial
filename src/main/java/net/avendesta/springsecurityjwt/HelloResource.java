package net.avendesta.springsecurityjwt;

import net.avendesta.springsecurityjwt.models.AuthenticationRequest;
import net.avendesta.springsecurityjwt.models.AuthenticationResponse;
import net.avendesta.springsecurityjwt.services.MyUserDetailService;
import net.avendesta.springsecurityjwt.services.util.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MyUserDetailService userDetailsService;
    @Autowired
    JWT jwtTokenUtil;
    @GetMapping("/")
    public String hello(){
        return "Hello world!";
    }
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (Exception e){
            throw new Exception("Incorrect username or password!");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
