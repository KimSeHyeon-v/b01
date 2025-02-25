package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder().title("title....." + i)
                    .content("content....."+i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BND: " + result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board.getBno());
        log.info("@@@@@@@@content : " + board.getContent());
    }

    @Test
    public void testUpdate(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update..title 100", "update..content 100");
        boardRepository.save(board);
    }

    @Test
    public void testDelete(){
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        // 1 page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : " + result.getTotalPages());
        log.info("total pages : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1(){

        //2 page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    public void testSearchAll2(){
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        log.info("total pages : " + result.getTotalPages());
        log.info("page size : " + result.getSize());
        log.info("page number : " + result.getNumber());
        log.info("prev : " + result.hasPrevious() + " / next : " + result.hasNext());
        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t", "c", "w"};
        String keyword = "update";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        //total page
        log.info(result.getTotalPages());
        // page Size
        log.info(result.getSize());
        // Page Number
        log.info(result.getNumber());
        // prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());;

        result.getContent().forEach(board -> log.info(board.toString()));
    }

    @Test
    public void testInsertWithImage(){
        Board board = Board.builder().title("Image Test").content("첨부파일 테스트").writer("tester").build();

        for (int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    // @Transactional 하나의 트랜잭션으로 묶어버림. 지연로딩은 처음 조회 후 seesion 을 종료시켜 에러가 발생함.
    public void testReadWithImage() {
        // 반드시  존재하는 bno
        Long bno = 1L;
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("----------------");
        for (BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }

    @Test
    @Transactional
    @Commit
    public void testModifyImages(){
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        //기존의 첨부파일 삭제
        board.clearImage();

        //새로운 첨부파일 등록
        for (int i = 0; i< 2; i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }
        boardRepository.save(board);

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1L;
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll(){
        for (int i = 0; i <= 100; i++){
            Board board = Board.builder().title("Title[" + i + "]").content("content[" + i + "]").writer("writer[" + i + "]").build();

            for(int j = 0; j < 3; j++){
                if(i % 5 == 0){
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Test
    @Transactional
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        /*boardRepository.searchWithAll(null, null, pageable);*/
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);
        log.info("-----------------");
        log.info(result.getTotalElements());
        result.getContent().forEach(boardListAllDTO -> {log.info(boardListAllDTO);});
    }
}
