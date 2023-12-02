package com.shoponlineback.user.password;

import com.shoponlineback.exceptions.user.password.InvalidOldPasswordException;
import com.shoponlineback.exceptions.user.password.PasswordsNotMatchException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.shoponlineback.user.UserService.getLoggedUser;

@Service
public class UserPasswordService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserPasswordService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Transactional
    public void changePassword(ChangeUserPasswordDto changeUserPasswordDto){
        User user = userRepository.findById(getLoggedUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getPassword() == null){
            throw new RuntimeException("Unfortunately, you cannot change your password as you are using social media authentication.");
        }
        if(!passwordEncoder.matches(changeUserPasswordDto.getOldPassword(), user.getPassword())){
            throw new InvalidOldPasswordException();
        }if (!changeUserPasswordDto.getNewPassword().equals(changeUserPasswordDto.getConfirmNewPassword())) {
            throw new PasswordsNotMatchException();
        }
        else{
            user.setPassword(passwordEncoder.encode(changeUserPasswordDto.getNewPassword()));
        }


    }
}
