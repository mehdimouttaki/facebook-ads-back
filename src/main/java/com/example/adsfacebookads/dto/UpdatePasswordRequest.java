package com.example.adsfacebookads.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    /**
     * old password of the user
     */
    private String oldPassword;

    /**
     * new password wished by the user
     */
    private String newPassword;

    /**
     * password confirmation that should match the new password
     */
    private String confirmPassword;

}
