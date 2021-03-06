package com.example.login.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(LocalDateTime time, String token) {
        confirmationTokenRepository.setConfirmedAt(time, token);
    }

}
