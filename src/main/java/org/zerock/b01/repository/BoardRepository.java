package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Board;
import org.zerock.b01.search.BoardSearch;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    Page<Board> search1(Pageable pageable);

}
