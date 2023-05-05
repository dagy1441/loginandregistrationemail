package com.dagy.loginandregistrationemail.token;

import com.dagy.loginandregistrationemail.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
        @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
        List<Token> findAllValidTokenByUser(Long id);

        Optional<Token> findByToken(String token);

        @Transactional
        @Modifying
        @Query("UPDATE Token t " +
                "SET t.confirmedAt = ?2 " +
                "WHERE t.token = ?1")
        int updateConfirmedAt(String token, LocalDateTime confirmedAt);

        void deleteByUser(User user);
}
