package org.zerock.b01.dto;

import lombok.*;

import javax.lang.model.element.Element;
import java.awt.*;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        if(total <= 0){
            return;
        }
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서 마지막 번호
        this.start = this.end - 9; //화면에서 시작번호
        int last = (int)(Math.ceil((total/(double)size))); // 데이터 개수를 계산한 페이지 마지막번호
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
