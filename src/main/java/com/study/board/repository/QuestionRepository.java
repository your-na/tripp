package com.study.board.repository;

import com.study.board.entity.Question;
import com.study.board.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {
    List<Question> findByAuthor(SiteUser author);
}
