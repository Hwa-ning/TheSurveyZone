package com.mongoosereum.dou_survey_zone.api.v1.domain.user;

import com.mongoosereum.dou_survey_zone.api.v1.dao.UserDAOImpl;
import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.SearchPWReq;
import com.mongoosereum.dou_survey_zone.api.v1.dto.request.user.SignUpReq;
import com.mongoosereum.dou_survey_zone.api.v1.exception.ErrorCode;
import com.mongoosereum.dou_survey_zone.api.v1.exception._401_Unauthorized.HasNotPermissionException;
import com.mongoosereum.dou_survey_zone.api.v1.exception._404_NotFound.NotFoundEntityException;
import com.mongoosereum.dou_survey_zone.security.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TokenProvider tokenProvider;

    public Integer createUser(final SignUpReq signUpReq){

        String encodedPassword = passwordEncoder.encode(signUpReq.getUser_Password());

        User user_mySQL = User.builder()
                .user_Email(signUpReq.getUser_Email())
                .user_Password(encodedPassword)
                .user_Name(signUpReq.getUser_Name())
                .user_Tel(signUpReq.getUser_Tel())
                .build();

        return userDAO.createUser_MySQL(user_mySQL);
    }

    public boolean checkEmail(final String User_Email){
        return !Objects.equals(User_Email, userDAO.existsByEmail_MySQL(User_Email));
    }

    public List<?> login(final String email, final String password){
        User searchUser = userDAO.findByEmailAndPassword_MySQL(email)
                .orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_USER));
        String tempPW = (String) redisTemplate.opsForValue().get(email+" TempPW");
        ArrayList result = new ArrayList();
        if(passwordEncoder.matches(password, searchUser.getUser_Password())){
            result.add("UserPW");
            result.add(tokenProvider.create(searchUser));
            result.add(searchUser);
            return result;
        }
        else if(passwordEncoder.matches(password, tempPW)) {
            result.add("TempPW");
            result.add(tokenProvider.create(searchUser));
            result.add(searchUser);
            return result;
        }
        throw new HasNotPermissionException(ErrorCode.NOT_FOUND_USER);
    }

    public List<String> searchID(final String name, final String tel){
        User user_mySQL = User.builder()
                .user_Name(name)
                .user_Tel(tel)
                .build();

        List<String> searchEmail = userDAO.findByEmail(user_mySQL);

        if(searchEmail.isEmpty()){
            throw new NotFoundEntityException(ErrorCode.NOT_FOUND_USER);
        }

        return searchEmail;
    }
    public User findByEmail_Name_Tel(SearchPWReq searchPWReq){

        User user_mySQL =  User.builder()
                .user_Email(searchPWReq.getUser_Email())
                .user_Name(searchPWReq.getUser_Name())
                .user_Tel(searchPWReq.getUser_Tel())
                .build();

        User result = userDAO.findByEmail_Name_Tel(user_mySQL).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_USER));

        return result;
    }

    public void BlackListToken(HttpServletRequest request){
        //푸는 이유는 블랙리스트를 추가를 하기 위해서이다.
        // 헤더에서 JWT 를 받아옵니다.
        String token = tokenProvider.resolveToken((HttpServletRequest) request);

        if(token != null){
            // 전체를 뽑아내서 뽑아내는 토큰
            Jws<Claims> claims = tokenProvider.confirmToken(token);
            log.info("logout:"+(claims.getBody().getExpiration().getTime()-System.currentTimeMillis()));
            // BlackListToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            //.set(이름 (bt: 토큰) , value(), 익스파이어 시간, 시간 타입)
            redisTemplate.opsForValue()
                    .set("BT:" + token, "blacklist",
                            // Date 타입(LONG) - 현재시간 () = 유효기간 남은시간() 만큼난 REDIS에서 쳐 들고잇는다.
                            (claims.getBody().getExpiration().getTime()-System.currentTimeMillis()),
                            TimeUnit.MILLISECONDS);
        }

    }

    //임시 비밀번호 발금
    public String makeTempPW(String User_Email) {

        int leftLimit = 48;
        int rightLimit = 122;
        int tempPW_length = 10;
        Random random = new Random();
        String tempPW = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(tempPW_length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String encodedPassword = passwordEncoder.encode(tempPW);

        redisTemplate.opsForValue()
                .set(User_Email+" TempPW", encodedPassword,
                        10,
                        TimeUnit.MINUTES);

        return tempPW;
    }
}
