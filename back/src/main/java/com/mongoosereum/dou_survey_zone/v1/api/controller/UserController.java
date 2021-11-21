package com.mongoosereum.dou_survey_zone.v1.api.controller;

import com.mongoosereum.dou_survey_zone.v1.api.security.TokenProvider;
import com.mongoosereum.dou_survey_zone.v1.api.user.dao.UserDAO;
import com.mongoosereum.dou_survey_zone.v1.api.user.dto.UserDTO;
import com.mongoosereum.dou_survey_zone.v1.api.user.entity.User_MySQL;
import com.mongoosereum.dou_survey_zone.v1.api.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="사용자 API", tags = {"User API"})
@RestController
@Slf4j
@RequestMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController{

    @Autowired
    private UserService Service;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path="/checkEmail")
    @ApiOperation(value = "이메일 중복검사")
    public ResponseEntity CheckEmail(
            @RequestBody
            @ApiParam(value="UserDTO",required = true )
                    UserDTO userDTO
    ) {
        System.out.println("check : " + userDTO.getUser_Email());
        return ResponseEntity.ok().body(Service.checkEmail(userDTO.getUser_Email()));
    }

    @PostMapping(path="/signup")
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<?> registerUser(
            @RequestBody
            @ApiParam(value="UserDTO",required = true )
                    UserDTO userDTO
    ) {
        // TODO 정환, 서비스로 이동 필요
        System.out.println("signup");
        System.out.println("username : " + userDTO.getUser_Name());
        String encodedPassword = passwordEncoder.encode(userDTO.getUser_Password());

        User_MySQL user = User_MySQL.builder()
                .user_Email(userDTO.getUser_Email())
                .user_Password(encodedPassword)
                .user_Name(userDTO.getUser_Name())
                .user_Tel(userDTO.getUser_Tel())
                .build();

        Integer result;
        try{
        result = Service.createUser(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
        if(result == 1) {
            UserDTO responseUserDTO = userDTO;
            return ResponseEntity.ok().body(responseUserDTO);
        }
        return ResponseEntity.badRequest().body("fail");
    }

    @PostMapping(path="/signin")
    @ApiOperation(value = "로그인")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {

        System.out.println("signin");
        System.out.println(userDTO.getUser_Email());
        System.out.println(userDTO.getUser_Password());

        User_MySQL user = Service.login(userDTO.getUser_Email(), userDTO.getUser_Password(), passwordEncoder);

        if(user != null){
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .user_Email(user.getUser_Email())
                    .user_Name(user.getUser_Name())
                    .user_Token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else{
            return ResponseEntity.badRequest().body("Login fail");
        }
    }

    /* 테스트용 */
    @GetMapping(path="/test")
    public String test(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO.getUser_Email());
        System.out.println(userDTO.getUser_Password());
        return "success";
    }
}