package com.shoponlineback.user.password;

import com.shoponlineback.exceptions.user.password.InvalidOldPasswordException;
import com.shoponlineback.exceptions.user.password.PasswordsNotMatchException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shoponlineback.user.UserService.getLoggedUser;

@Service
public class UserPasswordService {
    private final PasswordEncoder passwordEncoder;

    public UserPasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void changePassword(ChangeUserPasswordDto changeUserPasswordDto){
        User loggedUser = getLoggedUser();
        if(!passwordEncoder.matches(changeUserPasswordDto.getOldPassword(), loggedUser.getPassword())){
            throw new InvalidOldPasswordException();
        } else if (changeUserPasswordDto.getNewPassword().equals(changeUserPasswordDto.getConfirmNewPassword())) {
            throw new PasswordsNotMatchException();
        }
        else{
            loggedUser.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
        }


    }
}
