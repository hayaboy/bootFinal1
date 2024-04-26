package org.zerock.b01.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.hibernate.criterion.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board=QBoard.board;

        JPQLQuery<Board> query = from(board);  //select ... from board

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
//        query.where(board.title.contains("1"));
        booleanBuilder.or(board.title.contains("11")); // title like ...
        booleanBuilder.or(board.content.contains("11")); // content like ....

        query.where(booleanBuilder);

        query.where(board.bno.gt(0L));


        List<Board> list= query.fetch();


        long count=query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);


        if( (types != null && types.length > 0) && keyword != null ) { //검색 조건과 키워드가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types) {
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }


            }
            query.where(booleanBuilder);
            }

        //bno > 0
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list=query.fetch();
        long count=query.fetchCount();


        return new PageImpl<>(list, pageable,count);
        //return null;
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board=QBoard.board;
        QReply reply= QReply.reply;

        JPQLQuery<Board> query= from(board);

        //게시물과 댓글의 경우 한쪽에만 데이터가 존재하는 상황이 발생
        //그래서  outer join을 통해서 처리함

        //        from
        //        board board0_
        //        left outer join
        //        reply reply1_
        //        on (
        //                reply1_.board_bno=board0_.bno
        //        )

        query.leftJoin(reply).on(reply.board.eq(board));

        //조인 후에 게시물당 처리가 필요하므로 groupBy를 적용
        query.groupBy(board);


        //검색 조건

        if( (types != null && types.length > 0) && keyword != null ) { //검색 조건과 키워드가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types) {
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }


            }
            query.where(booleanBuilder);
        }

        //bno > 0
        query.where(board.bno.gt(0L));






        // Projections.bean()
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(
                BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")

        ));


        this.getQuerydsl().applyPagination(pageable,dtoQuery);


        List<BoardListReplyCountDTO> dtoList=dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
