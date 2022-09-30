package com.joanna.staybooking.service;

import com.joanna.staybooking.exception.UserAlreadyExistException;
import com.joanna.staybooking.model.Authority;
import com.joanna.staybooking.model.User;
import com.joanna.staybooking.model.UserRole;
import com.joanna.staybooking.repository.AuthorityRepository;
import com.joanna.staybooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service    //为了让Spring做dependency injection
public class RegisterService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired  //inject dependency with constructor, 也可以加在fields上 （使用object）
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //@Transactional 保证在数据库的操作是原子性的
    //Isolation.SERIALIZABLE 是最高级别的Isolation
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserAlreadyExistException {   //在数据库里对table进行写操作 //add()会被controller调用
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }

}
