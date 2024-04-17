package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.exceptions.UserNotEnabledException;
import com.ferraro.JobPlatform.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username){
        Account account = accountService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non registrato ".concat(username)));
        if(!account.isEnabled()){
            throw new UserNotEnabledException();
        }
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(account.getRole().toString()));
        return new org.springframework.security.core.userdetails.User(username, account.getPassword(), authorities);
    }
}
