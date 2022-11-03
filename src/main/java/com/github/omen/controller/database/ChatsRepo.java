package com.github.omen.controller.database;

import com.github.omen.controller.database.entities.Chat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatsRepo extends CrudRepository<Chat, Integer> {
    List<Chat> findChatsByOwnerIdEquals(int ownerId);

    Chat findChatByChatIdEquals(int chatId);

    Chat findChatByOwnerIdEqualsAndChatNameEquals(int ownerId, String chatName);

    boolean existsChatByOwnerIdEqualsAndChatNameEquals(int ownerId, String chatName);
}
