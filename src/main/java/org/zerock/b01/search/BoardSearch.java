package org.zerock.b01.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListReplyCountDTO;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    //페이징 후 서칭
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    //댓글 개수 추가 후 서칭
    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);


}
