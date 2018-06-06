package com.paytm.core.repository;

import com.paytm.core.domain.UserEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends CassandraRepository<UserEvent, String> {
    @Query("select count(*) from user_event where username = ?0 and tweetid = ?1 Limit 1")
    int exists(String username, String tweetId);
}
