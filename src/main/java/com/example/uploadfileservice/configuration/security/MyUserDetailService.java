package com.example.uploadfileservice.configuration.security;

import com.example.uploadfileservice.model.entity.UserEntity;
import com.example.uploadfileservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userInDB = userRepository.findUserEntitiesWithPermission(username).orElseThrow(() -> new UsernameNotFoundException(""));
        List<GrantedAuthority> list = new ArrayList<>();
        userInDB.getRole().getPermissions().forEach(permissionEntity -> {
            list.add(new SimpleGrantedAuthority(permissionEntity.getPermissionName()));
        });
        return new User(username, userInDB.getPassword(), list);
    }
}
