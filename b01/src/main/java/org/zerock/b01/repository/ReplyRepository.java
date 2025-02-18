package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Reply;

//JpaRepository를 상속 받는 것 만으로도 어느정도 CRUD와 페이징이 가능 TEST 용도로 개발단계에 적용하기 용이하다.
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(@Param("bno") Long bno, Pageable pageable);

    void deleteByBoard_Bno(@Param("bno") Long bno);
}
