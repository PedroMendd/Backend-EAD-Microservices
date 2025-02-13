package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtRecordDto;
import com.ead.authuser.dtos.LoginRecordDto;
import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final UserService userService;
    final UserValidator userValidator;
    final AuthenticationManager authenticationManager;
    final JwtProvider jwtProvider;

    public AuthenticationController(UserService userService, UserValidator userValidator, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @Validated(UserRecordDto.UserView.RegistrationPost.class)
                                               @JsonView(UserRecordDto.UserView.RegistrationPost.class)
                                               UserRecordDto userRecordDto, Errors errors){

        log.debug("POST registerUser userRecordDto received {}", userRecordDto);
        userValidator.validate(userRecordDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtRecordDto> authenticateUser(@RequestBody @Valid LoginRecordDto loginRecordDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRecordDto.username(), loginRecordDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtRecordDto(jwt));
    }

//    @GetMapping("/logs")
//    public String index(){
//        log.trace("TRACE");
//        log.debug("DEBUG");
//        log.info("INFO");
//        log.warn("WARN");
//        log.error("ERROR");
//        return "Logging Spring Boot...";
//    }

}
