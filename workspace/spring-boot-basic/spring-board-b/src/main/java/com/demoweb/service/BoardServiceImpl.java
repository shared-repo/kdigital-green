package com.demoweb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demoweb.dao.BoardDao;
import com.demoweb.dto.BoardAttachDto;
import com.demoweb.dto.BoardCommentDto;
import com.demoweb.dto.BoardDto;
import com.demoweb.entity.BoardEntity;
import com.demoweb.mapper.BoardCommentMapper;
import com.demoweb.mapper.BoardMapper;
import com.demoweb.repository.BoardRepository;

import lombok.Setter;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	@Qualifier("boardDao")
	private BoardDao boardDao;
	
	@Autowired
	@Qualifier("boardMapper")
	private BoardMapper boardMapper;
	
	@Autowired
	@Qualifier("boardCommentMapper")
	private BoardCommentMapper commentMapper;
	
	@Autowired
	private BoardRepository boardRepository;
	
	// 사용자가 입력한 게시글 데이터를 받아서 글쓰기 처리
	// @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void writeBoard(BoardDto board) {
		
//		boardMapper.insertBoard(board); // insert 하면서 boardNo 자동 생성 ( 글 번호 가져오기 필요 )
//		
//		if (board.getAttachments() != null) {
//			for (BoardAttachDto attachment : board.getAttachments()) {
//				attachment.setBoardNo(board.getBoardNo()); // 새로 만들어진 글번호를 Attach 객체에 저장
//				boardDao.insertBoardAttach(attachment);
//			}
//		}
		
		// 게시글 등록 작업만 처리 ( 첨부파일 처리는 나중에 )
 		BoardEntity boardEntity = BoardEntity.builder()
 											 .title(board.getTitle())
 											 .writer(board.getWriter())
 											 .content(board.getContent())
 											 .build();
 		boardRepository.save(boardEntity);
		
	}
	
	// 모든 게시글 조회해서 반환
	@Override
	public List<BoardDto> findAllBoard() {
		
		// List<BoardEntity> boardList = boardRepository.findAll();
		List<BoardEntity> boardList = boardRepository.findAllByOrderByBoardNoDesc();
		ArrayList<BoardDto> boards = new ArrayList<>();
		for (BoardEntity boardEntity : boardList) {
			boards.add(boardEntity.exportBoardDto());
		}
		
		return boards;
		
	}
	
	@Override
	public List<BoardDto> findBoardByPage(int pageNo, int pageSize) {
		
		int from = (pageNo - 1) * pageSize;
		int count = pageSize;
		
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<BoardEntity> page = boardRepository.findAll(pageRequest);
		
		List<BoardEntity> boardList = boardRepository.findAllWithPage(from, count);
		
		ArrayList<BoardDto> boards = new ArrayList<>();
		for (BoardEntity boardEntity : boardList) {
			boards.add(boardEntity.exportBoardDto());
		}
		
		return boards;
		
	}
	
	@Override
	public HashMap<String, Object> findBoardByPage2(int pageNo, int pageSize) {
		
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<BoardEntity> page = boardRepository.findAll(pageRequest);
		
		HashMap<String, Object> pagingData = new HashMap<>();
		pagingData.put("dataCount", (int)page.getTotalElements());
		pagingData.put("pageCount", page.getTotalPages());
		List<BoardEntity> boardList = page.getContent();
		
		ArrayList<BoardDto> boards = new ArrayList<>();
		for (BoardEntity boardEntity : boardList) {
			boards.add(boardEntity.exportBoardDto());
		}
		
		pagingData.put("boards", boards);
		
		return pagingData;
	}
	
	// 글 번호를 받아서 게시글 조회 및 반환
	@Override
	public BoardDto findBoardByBoardNo(int boardNo) {
		
		// Board와 BoardAttach를 각각 조회
//		BoardDto board = boardMapper.selectBoardByBoardNo(boardNo);		
//		if (board != null) {
//			List<BoardAttachDto> attachments = boardMapper.selectBoardAttachByBoardNo(boardNo);
//			board.setAttachments(attachments);
//		}
		
		// Board와 BoardAttach를 한 번에 조회
		BoardDto board = boardMapper.selectBoardByBoardNo2(boardNo);
		
		// Comment 조회
		
		return board;
		
	}
	
	// 글 번호를 받아서 게시글 조회수 증가
	@Override
	public void increaseBoardReadCount(int boardNo) {
		
		boardDao.updateBoardReadCount(boardNo);
		
	}
	
	// 글 번호를 받아서 게시글 삭제
	@Override
	public void deleteBoard(int boardNo) {
		
		boardDao.deleteBoard(boardNo);
		
	}
	
	// 첨부파일 번호를 받아서 첨부파일 데이터 조회 및 반환
	@Override
	public BoardAttachDto findBoardAttachByAttachNo(int attachNo) {
		
		BoardAttachDto attachment = boardDao.selectBoardAttachByAttachNo(attachNo);
		return attachment;
		
	}

	@Override
	public long findBoardCount() {
		
		long boardCount = boardRepository.countBy();
		return boardCount;
		
	}
	
	@Override
	public void modifyBoard(BoardDto board) {
		
		boardDao.updateBoard(board);
		
	}

	@Override
	public void writeComment(BoardCommentDto comment) {
		
		commentMapper.insertComment(comment);
		
	}

	@Override
	public void updateGroupNo(int commentNo, int groupNo) {
		
		commentMapper.updateGroupNo(commentNo, groupNo);
		
	}

	@Override
	public void deleteComment(int commentNo) {
		
		commentMapper.deleteComment(commentNo);
		
	}

	@Override
	public List<BoardCommentDto> findBoardCommentByBoardNo(int boardNo) {
		
		List<BoardCommentDto> comments = commentMapper.selectCommentByBoardNo(boardNo);
		return comments;
		
	}

	@Override
	public void updateComment(BoardCommentDto comment) {
		
		commentMapper.updateComment(comment);
		
	}

	@Override
	public void writeReComment(BoardCommentDto commentDto) {

		// 1. 부모글 조회 -> 그룹번호(groupno), 그룹내 순서번호(step), 들여쓰기 (depth) 적용
		BoardCommentDto parent = commentMapper.selectCommentByCommentNo(commentDto.getCommentNo());
		commentDto.setBoardNo(parent.getBoardNo());
		commentDto.setGroupNo(parent.getGroupNo());
		commentDto.setStep(parent.getStep() + 1);
		commentDto.setDepth(parent.getDepth() + 1);
		
		// 2. 이미 등록된 글 중에서 삽입될 위치 뒤에 있는 글의 step 조정 (1 증가)
		commentMapper.updateStepNo(parent.getGroupNo(), parent.getStep());
		
		// 3. 글쓰기
		commentMapper.insertReComment(commentDto);
	}


}










