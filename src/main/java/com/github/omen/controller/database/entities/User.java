package com.github.omen.controller.database.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Getter
    @Setter
    @Column(unique = true)
    private String userName;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private int colourSchemeId;
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longitude;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
