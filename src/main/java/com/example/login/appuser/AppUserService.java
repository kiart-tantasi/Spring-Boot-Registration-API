package com.example.login.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

        private static String USER_NOT_FOUND = "user %s not found";
        private final AppUserRepository appUserRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return appUserRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        }

        public Boolean emailExists(String email) {
                return appUserRepository.findByEmail(email).isPresent();
        }

        public void save(AppUser appUser) {
                appUserRepository.save(appUser);
        }

        public void enableUser(String email) {
                appUserRepository.enableUser(email);
        }
}