package com.qingchuan.coderman.web.cache;

import com.github.pagehelper.PageHelper;
import com.qingchuan.coderman.web.dao.QuestionMapper;
import com.qingchuan.coderman.web.modal.Question;
import com.qingchuan.coderman.web.modal.QuestionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class TagTask {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private  RedisTemplate redisTemplate;
//    private StringRedisTemplate redisTemplate;
//    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private HotTagCache hotTagCache;

    //定时任务：5个小时更新一次热门
    @Scheduled(fixedRate = 10000*6*5)
    public void printTest() {
        int pageNo = 1;
        int pageSize = 10;
        List<Question> questions = new ArrayList<>();
        Map<String, Integer> properties = new HashMap<>();
        while (pageNo == 1 || questions.size() == pageSize) {
            //questions = questionExtMapper.findQuestionPage((pageNo - 1) * pageSize, pageSize);
            PageHelper.startPage(pageNo,pageSize);
            QuestionExample example = new QuestionExample();
            example.setOrderByClause("gmt_create desc");
            questions=questionMapper.selectByExample(example);
            for (Question question : questions) {
                String tagstr = question.getTag();
                String[] tags={};
                if(!"".equals(tagstr)&&tagstr!=null){
                    tags= tagstr.split(",");
                }
                for (String tag : tags) {
                    tag=tag.trim().toLowerCase();
                    Integer integer = properties.get(tag);
                    if (null!=integer) {
                        properties.put(tag, integer + 5 + question.getCommentCount());
                    } else {
                        properties.put(tag, 5 + question.getCommentCount());
                    }
                }
            }
            pageNo++;
        }
        hotTagCache.setProperties(properties);

        //按权重排序
        List<String> tags = hotTagCache.updateTags();

        //清除之前的缓存
        redisTemplate.delete("hot");

        //放到redis中
        for (String tag : tags) {
            redisTemplate.opsForList().rightPush("hot",tag);
        }
    }

}
