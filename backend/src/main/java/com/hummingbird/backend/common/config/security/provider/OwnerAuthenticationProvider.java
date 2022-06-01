package com.hummingbird.backend.common.config.security.provider;

import com.hummingbird.backend.common.config.security.service.OwnerAuthenticationContext;
import com.hummingbird.backend.common.config.security.service.OwnerDetailsService;
import com.hummingbird.backend.common.config.security.token.OwnerAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class OwnerAuthenticationProvider implements AuthenticationProvider {

    private final OwnerDetailsService ownerDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String tryLoginEmail = authentication.getName();
        String tryLoginPassword = (String) authentication.getCredentials();

        OwnerAuthenticationContext ownerAuthenticationContext = (OwnerAuthenticationContext) ownerDetailsService.loadUserByUsername(tryLoginEmail);

        if (!passwordEncoder.matches(tryLoginPassword, ownerAuthenticationContext.getPassword())) {
          throw new BadCredentialsException("Invalid password");
        }
        if (Boolean.TRUE.equals(ownerAuthenticationContext.getOwner().getIsRemoved())){
            throw new DisabledException("삭제된 유저입니다. 고객센터에 문의하세요");
        }
        return new OwnerAuthenticationToken(ownerAuthenticationContext.getOwner(),null,ownerAuthenticationContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(OwnerAuthenticationToken.class);
    }
}
