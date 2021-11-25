package com.mongoosereum.dou_survey_zone.api.v1.controller;

import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.CheckEmailReq;
import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.SearchIDReq;
import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.SignUpReq;
import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.SigninReq;
import com.mongoosereum.dou_survey_zone.api.v1.dto.response.user.SignInRes;
import com.mongoosereum.dou_survey_zone.security.TokenProvider;
import com.mongoosereum.dou_survey_zone.api.v1.domain.user.User;
import com.mongoosereum.dou_survey_zone.api.v1.domain.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="사용자 API", tags = {"User API"})
@RestController
@Slf4j
@RequestMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController{

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(path="/checkEmail")
    @ApiOperation(value = "이메일 중복검사")
    public ResponseEntity CheckEmail(
            @RequestBody
            @ApiParam(value="CheckEmailReq",required = true )
                    CheckEmailReq checkEmailReq
    ) {
        System.out.println(checkEmailReq.getUser_Email());
        return ResponseEntity.ok().body(userService.checkEmail(checkEmailReq.getUser_Email()));
    }

    @PostMapping(path="/signup")
    @ApiOperation(value = "회원 가입")
    public ResponseEntity registerUser(
            @RequestBody
            @ApiParam(value="SignUpReq",required = true )
                    SignUpReq signUpReq
    ) {
        Integer result;
        try{
            result = userService.createUser(signUpReq);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
        if(result == 1) {
            return ResponseEntity.ok().body(signUpReq);
        }
        return ResponseEntity.badRequest().body("fail");
    }

    @PostMapping(path="/signin")
    @ApiOperation(value = "로그인")
    public ResponseEntity signin(@RequestBody SigninReq SigninReq) {

        User user = userService.login(SigninReq.getUser_Email(), SigninReq.getUser_Password());

        if(user != null){
            final String token = tokenProvider.create(user);
            final SignInRes signInRes = SignInRes.builder()
                    .user_Email(user.getUser_Email())
                    .user_Name(user.getUser_Name())
                    .user_Token(token)
                    .build();
            return ResponseEntity.ok().body(signInRes);
        } else{
            return ResponseEntity.badRequest().body("Login fail");
        }
    }

    @PostMapping(path="/searchID")
    @ApiOperation(value = "ID찾기")
    public ResponseEntity searchID(@RequestBody SearchIDReq searchIDReq) {
        List<String> user = userService.searchID(searchIDReq.getUser_Name(), searchIDReq.getUser_Tel());
    return (user != null) ? ResponseEntity.ok().body(user) :  ResponseEntity.badRequest().body("NO User");
    }


    /* 테스트용 */
    @GetMapping(path="/test")
    public String test() {
        return "success";
    }
}