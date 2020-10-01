package com.qingchuan.coderman.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qingchuan.coderman.admin.service.CommentManagerService;
import com.qingchuan.coderman.admin.vo.CommentVO;
import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.web.dao.CommentMapper;
import com.qingchuan.coderman.web.modal.Comment;
import com.qingchuan.coderman.web.modal.CommentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class CommentManagerServiceImpl implements CommentManagerService {


    @Autowired
    private CommentMapper commentMapper;


    @Override
    public PageVO<Comment> list(CommentVO commentVO) {
        PageHelper.startPage(commentVO.getPage(),commentVO.getLimit());
        CommentExample example = new CommentExample();
        if(commentVO.getContent()!=null&&!"".equals(commentVO.getContent())){
            example.createCriteria().andContentLike("%"+commentVO.getContent()+"%");
        }
        List<Comment> comments = commentMapper.selectByExample(example);
        PageInfo<Comment> info=new PageInfo<>(comments);
        return new PageVO<>(info.getTotal(),info.getList());
    }

    @Override
    public void delete(Integer id) {
        commentMapper.deleteByPrimaryKey(id);
    }
}
