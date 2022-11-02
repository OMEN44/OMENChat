package com.github.omen.controller.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @Getter
    @Setter
    private int memberId;
    @Getter
    @Setter
    private int userId;
    @Getter
    @Setter
    private int chatId;
}
