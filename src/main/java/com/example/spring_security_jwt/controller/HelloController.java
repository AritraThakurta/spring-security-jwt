package com.example.spring_security_jwt.controller;

import com.example.spring_security_jwt.model.AuthenticationRequest;
import com.example.spring_security_jwt.model.AuthenticationResponse;
import com.example.spring_security_jwt.service.MyUserDetailService;
import com.example.spring_security_jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateRequest(@RequestBody AuthenticationRequest authReq) throws Exception {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
        }
        catch (BadCredentialsException e){
            throw new Exception("Incorrect UserName or password",e);
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(authReq.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
