package com.dca.chat.repository;

import com.dca.chat.domain.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.channel.id = :channelId")
    Page<Message> findByChannelIdWithSender(@Param("channelId") Long channelId, Pageable pageable);

}