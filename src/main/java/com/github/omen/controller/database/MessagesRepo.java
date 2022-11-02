package com.github.omen.controller.database;

import com.github.omen.controller.database.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessagesRepo extends CrudRepository<Message, Integer> {

}
