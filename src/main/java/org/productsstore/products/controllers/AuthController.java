package org.productsstore.products.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.productsstore.products.Dtos.LoginRequestDto;
import org.productsstore.products.Dtos.SignupRequestDto;
import org.productsstore.products.Dtos.UserDto;
import org.productsstore.products.Dtos.ValidateTokenRequestDto;
import org.productsstore.products.models.User;
import org.productsstore.products.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/signup")
    // API to register a new user in the system.
    // Accepts user details (name, email, password) and creates a new user account.
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        User user = authService.signup(signupRequestDto.getName(),signupRequestDto.getEmail(),signupRequestDto.getPassword());
        // Converts the User entity to a UserDto before returning the response
        return from(user);
    }

    @PostMapping("/login")
    // API to authenticate an existing user. Validates email and password, and returns user details along with an auth token.
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User,String> userWithToken =  authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        // Convert the authenticated User entity to UserDto
        UserDto userDto = from(userWithToken.a);
        // Extract the authentication token
        String token = userWithToken.b;
        // Prepare HTTP headers to include the token as a Set-Cookie response
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new ResponseEntity<>(userDto,headers, HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    // API to validate an authentication token against a specific user ID.
    public Boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        return authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
    }

    // Utility method to convert a User entity into a UserDto.
    // Ensures sensitive fields (like password) are not exposed in API responses.
    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        // Map required fields from User entity to UserDto
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
