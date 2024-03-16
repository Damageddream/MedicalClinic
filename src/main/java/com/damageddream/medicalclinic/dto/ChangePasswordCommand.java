package com.damageddream.medicalclinic.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
    public class ChangePasswordCommand {
    private String password;
}
