package com.example.przychodnia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    private String simplifiedRoleName;

    public Role(String roleName, String simplifiedRoleName) {
        this.roleName = roleName;
        this.simplifiedRoleName = simplifiedRoleName;
    }
}
