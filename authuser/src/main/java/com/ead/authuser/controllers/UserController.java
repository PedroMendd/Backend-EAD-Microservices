package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.AuthenticationCurrentUserService;
import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;
    final AuthenticationCurrentUserService authenticationCurrentUserService;
    final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, AuthenticationCurrentUserService authenticationCurrentUserService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       Pageable pageable){

        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()){
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (currentUserId.equals(userId)){
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        log.debug("DELETE deleteUser userId received {}", userId);
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody
                                             @Validated(UserRecordDto.UserView.UserPut.class)
                                             @JsonView(UserRecordDto.UserView.UserPut.class)
                                             UserRecordDto userRecordDto){

        log.debug("PUT updateUser userRecordDto received {}", userRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDto, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody
                                                 @Validated(UserRecordDto.UserView.PasswordPut.class)
                                                 @JsonView(UserRecordDto.UserView.PasswordPut.class)
                                                 UserRecordDto userRecordDto){
        log.debug("PUT updatePassword userId received {}", userId);
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (!passwordEncoder.matches(userRecordDto.oldPassword(), userModelOptional.get().getPassword())) {
            log.warn("Mismatched old password! userId {}", userId);
            return ResponseEntity.status((HttpStatus.CONFLICT)).body("Error: Mismatched old password!");
        }

        userService.updatePassword(userRecordDto, userModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody
                                             @Validated(UserRecordDto.UserView.ImagePut.class)
                                             @JsonView(UserRecordDto.UserView.ImagePut.class)
                                             UserRecordDto userRecordDto){

        log.debug("PUT updateImage userRecordDto {}", userRecordDto);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDto, userService.findById(userId).get()));
    }

}
