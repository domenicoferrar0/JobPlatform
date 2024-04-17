package com.ferraro.JobPlatform.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class LoginResponse {


    private String url;

    public LoginResponse(Collection<? extends GrantedAuthority> authority) {
        String url = "/error";
        for (GrantedAuthority a : authority) {
            switch (a.getAuthority()) {

                case "ROLE_USER":
                    url = "/user/home";
                    break;

                case "ROLE_EMPLOYER":
                    url = "/employer/home";
                    break;

                case "ROLE_ADMIN":
                    url = "/admin/home";
                    break;
            }

            this.url = url;
        }
    }

}
