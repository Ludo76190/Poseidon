package com.nnk.springboot.domain;

import com.nnk.springboot.configuration.validation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @ValidPassword
    private String password;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    private String role;

}
