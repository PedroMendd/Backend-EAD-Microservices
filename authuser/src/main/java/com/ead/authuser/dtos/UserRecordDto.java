package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecordDto(
                            @NotBlank(message = "Username is mandatory")
                            @Size(min = 4, max = 50, message = "Size must be between 4 and 50")
                            @JsonView(UserView.RegistrationPost.class)
                            String username,

                            @NotBlank(message = "Email is mandatory")
                            @Email(message = "Email must be in the expected format")
                            @JsonView(UserView.RegistrationPost.class)
                            String email,

                            @NotBlank(message = "Password is mandatory")
                            @Size(min = 6, max = 20, message = "Size must be between 6 and 20")
                            @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
                            String password,

                            @NotBlank(message = "Old password is mandatory")
                            @Size(min = 6, max = 20, message = "Size must be between 6 and 20")
                            @JsonView(UserView.PasswordPut.class)
                            String oldPassword,

                            @NotBlank(message = "Full Name is mandatory")
                            @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                            String fullName,

                            @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                            String phoneNumber,

                            @NotBlank(message = "Image URL is mandatory")
                            @JsonView(UserView.ImagePut.class)
                            String imageUrl) {

    public interface UserView {
        interface RegistrationPost {}
        interface UserPut {}
        interface PasswordPut {}
        interface ImagePut {}
    }

}
