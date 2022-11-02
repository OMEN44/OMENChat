package com.github.omen.controller.database;

import com.github.omen.controller.database.entities.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembersRepo extends CrudRepository<Member, Integer> {
    List<Member> findMembersByUserIdEquals(int user);
}
