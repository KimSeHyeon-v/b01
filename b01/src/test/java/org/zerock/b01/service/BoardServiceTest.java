package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.upload.BoardImageDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder().title("Sample").content("Sample").writer("user00").build();
        Long bno = boardService.register(boardDTO);

        log.info(bno);
    }

    @Test
    public void testModify(){
        //변경에 필요한 데이터
        BoardDTO boardDTO = BoardDTO.builder().bno(101L).title("update").content("update").build();
        // 첨부파일을 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().type("tcw").keyword("1").page(1).size(10).build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder().title("file name sample").content("sample Content").writer("uesr00").build();
        boardDTO.setFileNames(Arrays.asList(
                UUID.randomUUID()+"_aaa.jpg",
                UUID.randomUUID()+"_bbb.jpg",
                UUID.randomUUID()+"_ccc.jpg"));
        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 102L;
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        } // end for
    }

    @Test
    public void testRemoveAll(){
        Long bno = 1L;
        boardService.remove(bno);

    }

    @Test
    public void testListWithAll(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());
            if(boardListAllDTO.getBoardImages() != null ){
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()){
                    log.info(boardImage);
                }
            }
            log.info("-----------------");
        });
    }











}
