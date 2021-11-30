package com.mongoosereum.dou_survey_zone.api.v1.dao;

import com.mongoosereum.dou_survey_zone.api.v1.domain.user.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserDAO  {

    @Autowired
    SqlSession sqlSession;

    public int createUser_MySQL(User user_MySQL) {
        return sqlSession.insert("createUser", user_MySQL);
    }

    public Optional<User> existsByEmail_MySQL(String email) {
        return Optional.ofNullable(sqlSession.selectOne("existsByEmail", email));
    }

    public User findByEmailAndPassword_MySQL(String email) {
        return sqlSession.selectOne("findByEmailAndPassword", email);
    }

    public List<String> findByEmail(User user_MySQL) {
        return sqlSession.selectList("findByEmail", user_MySQL);
    }

    public int findByEmail_Name_Tel(User user){
        return sqlSession.selectOne("findByEmail_Name_Tel",user);
    }
    public int setPW(User user){
        return sqlSession.update("setPW",user);
    }
}
