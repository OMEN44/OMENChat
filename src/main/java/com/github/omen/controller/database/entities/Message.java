package com.github.omen.controller.database.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Message {
    @Id
    private int id;
    private int senderId;
    private String content;
    private Date date;
    //this can be a chat or a user
    private int recipientId;
}
