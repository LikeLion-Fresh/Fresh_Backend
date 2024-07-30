package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//CRUD함수를 JpaRepository가 들고 있다
public interface UserRepository extends JpaRepository<User, Integer> {
    //findBy규칙 Username문법 findBy가 진짜 찾아주는 명령어임 따로 그시기 할 필요가 없음
    User findByUsername(String username);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
