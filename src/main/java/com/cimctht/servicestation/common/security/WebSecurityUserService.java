package com.cimctht.servicestation.common.security;

import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.repository.RoleRepository;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebSecurityUserService  implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.cimctht.servicestation.user.entity.User user = userRepository.queryUserByLoginNameAndIsDeleteAndIsLocked(username,0, 0);
        String dbPassword = passwordEncoder.encode(user.getPassword());
        // 获取用户的角色
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 角色必须以`ROLE_`开头
        List<Role> listRoles = roleRepository.queryRolesByUserLoginname(username);
        for(Role r : listRoles) {
            authorities.add(new SimpleGrantedAuthority(r.getCode()));
        }
        return new User(username,dbPassword,true,true,true,true,authorities);
    }
}
