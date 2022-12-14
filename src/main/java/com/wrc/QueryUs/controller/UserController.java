package com.wrc.QueryUs.controller;

import com.wrc.QueryUs.dto.ApiResponse;
import com.wrc.QueryUs.dto.RegisterDto;
import com.wrc.QueryUs.dto.UserDto;
import com.wrc.QueryUs.service.TokenService;
import com.wrc.QueryUs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterDto registerDto, Errors errors) {

        if (errors.getAllErrors().size() > 0) {
            return new ResponseEntity<>(new ApiResponse(errors.getAllErrors().get(0).getDefaultMessage(), false), HttpStatus.BAD_REQUEST);
        }
        log.info(registerDto.toString());
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return new ResponseEntity<>(new ApiResponse("Password do not match.", false), HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(registerDto);
        return new ResponseEntity<>(new ApiResponse("User created successfully.", true), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable int id) {
        UserDto u = userService.getUser(id);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyUser(@RequestParam("token") String token){
        if(token.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("Invalid Token",false),HttpStatus.BAD_REQUEST);

        }
        tokenService.confirmToken(token);
        return new ResponseEntity<>(new ApiResponse("Email Verified",true),HttpStatus.OK);

    }

}
