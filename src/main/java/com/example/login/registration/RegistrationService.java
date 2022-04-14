package com.example.login.registration;

import com.example.login.appuser.AppUser;
import com.example.login.appuser.AppUserService;
import com.example.login.emailsender.EmailSender;
import com.example.login.registration.token.ConfirmationToken;
import com.example.login.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;

    public String register(RegistrationRequest registrationRequest) {

        // TODO: check if email is valid

        // check if email is already used
        Boolean emailUsed = appUserService.emailExists(registrationRequest.getEmail());
        if (emailUsed) throw new IllegalStateException("email is already taken");

        String encodedPassword = bCryptPasswordEncoder.encode(registrationRequest.getPassword());

        // create new user
        AppUser newAppUser = new AppUser(
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getEmail(),
                encodedPassword
        );
        appUserService.save(newAppUser);

        // create Token
        String token = UUID.randomUUID().toString();
        ConfirmationToken newConfirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                newAppUser
        );
        confirmationTokenService.save(newConfirmationToken);

        // TODO: send email
        String confirmationLink = "http://localhost:8080/api/registration?token=" + token;
        emailSender.sendEmail(
                registrationRequest.getEmail(),
                buildEmail(
                        registrationRequest.getFirstName(),
                        registrationRequest.getLastName(),
                        confirmationLink
                )
        );
        return "Please confirm your e-mail";
    }

    @Transactional // for setConfirmedAt and enableUser
    public String confirmUser(String token) {
        ConfirmationToken confirmationToken
            = confirmationTokenService.getConfirmationTokenByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        // check if token is already confirmed
        if (confirmationToken.getConfirmedAt() != null) throw new IllegalStateException("This token or email is already confirmed.");

        // stamp confirm time
        confirmationTokenService.setConfirmedAt(LocalDateTime.now(), token);

        // enable user
        String userEmail = confirmationToken.getAppUser().getEmail();
        appUserService.enableUser(userEmail);

        return "Confirmed Successfully";
    }


    // creating e-mail (HTML)
    private String buildEmail(String firstName, String lastName, String link) {
        return "<div>" +
                "<h1> Hi " + firstName + " " + lastName + " !" +
                "<br/>" +
                "<h1>Your Confirmation Link: </h1>" +
                "<a href=\"" + link + "\"> Click here to Confirm ! </a>" +
                "<br/>" +
                "</div>";
    }
}
