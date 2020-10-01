package com.qingchuan.coderman.web.controller;

import com.qingchuan.coderman.web.cache.TagsCache;
import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.web.modal.Question;
import com.qingchuan.coderman.web.modal.User;
import com.qingchuan.coderman.web.myenums.CustomizeErrorCode;
import com.qingchuan.coderman.web.myenums.QuestionErrorEnum;
import com.qingchuan.coderman.web.service.QuestionService;
import com.qingchuan.coderman.web.service.TopicService;
import com.qingchuan.coderman.web.utils.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 发布问题
 */
@Controller
@SuppressWarnings("all")
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TopicService topicService;

    private static final SensitiveWord sensitiveWord = new SensitiveWord();

    /**
     * 发布问题
     */
    @GetMapping("/publish")
    public String publish(Map<String,Object> map, Model model) {
        //Tags
        List<TagsCache> tagsCache = TagsCache.getTagsCache();
        model.addAttribute("tagsCache",tagsCache);
        //话题数据
        map.put("topiclist",topicService.listAllTopic());
        map.put("navLi","publish");
        return "publish";
    }

    @ResponseBody
    @PostMapping("/publish")
    public ResultTypeDTO doPublish(@RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("tag") String tag,
                                   @RequestParam(name = "id",required = false) Integer id,
                                   @RequestParam(name= "category") Integer category,
                                   @RequestParam(name= "topic",required = false) Integer topic,
                                   @RequestParam(name = "bool", required = false) boolean bool,
                                   Map<String, Object> map,
                                   HttpServletRequest request,
                                   Model model) {
        User user = (User) request.getSession().getAttribute("user");
        // 验证用户登入
        if (user == null) {
            return new ResultTypeDTO().errorOf(QuestionErrorEnum.QUESTION_NEED_LOGIN);
        }
        //标题是否合理
        if(title==null||"".equals(title.trim())){
            return new ResultTypeDTO().errorOf(QuestionErrorEnum.QUESTION_HEAD_CANT_EMPTY);
        }else {
            title = sensitiveWord.filterInfo(title);
            if(sensitiveWord.sensitiveWordSet.size()>0){
                return new ResultTypeDTO().errorOf("标题包含敏感词："+sensitiveWord.sensitiveWordSet);
            }
        }
        // 标题太简单
        if(title.trim().length()<1){
            return new ResultTypeDTO().errorOf(CustomizeErrorCode.TITLE_IS_TOO_SIMPLE);
        }
        // 分类为空
        if(category==0){
            return new ResultTypeDTO().errorOf(QuestionErrorEnum.Question_Category_CANT_EMPTY);
        }
        if(description==null||"".equals(description.trim())){
            return new ResultTypeDTO().errorOf(QuestionErrorEnum.QUESTION_DESC_CANT_EMPTY);
        }else{
            description = sensitiveWord.filterInfo(description);
            if(sensitiveWord.sensitiveWordSet.size()>0){
                return new ResultTypeDTO().errorOf("内容包含敏感词："+sensitiveWord.sensitiveWordSet);
            }
        }
        if(tag==null||"".equals(tag.trim())){
           return new ResultTypeDTO().errorOf(QuestionErrorEnum.QUESTION_TAGS_CANT_EMPTY);
        }else {
            String[] tags = tag.split(",");
            for (String t : tags) {
                sensitiveWord.filterInfo(t);
                if(sensitiveWord.sensitiveWordSet.size()>0){
                    return new ResultTypeDTO().errorOf("标签包含敏感词："+sensitiveWord.sensitiveWordSet);
                }
            }
        }

        Question question = new Question();
        if(bool == true){ // true为匿名，将创建者设置为匿名用户
            question.setCreator(1);
        } else { //不匿名就是正常的
            question.setCreator(user.getId());
        }
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setId(id);
        question.setCategory(category);
        question.setTopic(topic);
        question.setBool(bool);
        question.setUser(user);
        ResultTypeDTO result= null;
        try {
            result = questionService.saveOrUpdate(question);
        } catch (Exception e) {
            return  new ResultTypeDTO().errorOf(CustomizeErrorCode.NOT_ADD_OTHER_BQ);
        }
        //questionService.doPublish(question);
       // return "redirect:/";
        return result;
    }

    /**
     * 修改问题
     */
    @GetMapping("/publish/{id}")
    public String editQuestion(@PathVariable("id") Integer id,Map<String,Object> map,Model model){
        Question question =questionService.findQuestionById(id);
        List<TagsCache> tagsCache = TagsCache.getTagsCache();
        model.addAttribute("tagsCache",tagsCache);
        map.put("title",question.getTitle());
        map.put("description",question.getDescription());
        map.put("tag",question.getTag());
        map.put("id",id);
        map.put("category",question.getCategory());
        return "publish";
    }

    @PostMapping("/publish/{id}")
    public String doUpdate(@PathVariable("id") Integer id,@RequestParam("title") String title,
                           @RequestParam("description") String description,
                           @RequestParam("tag") String tag){
        Question question=questionService.findQuestionById(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setGmtModified(System.currentTimeMillis());
        question.setTag(tag);
        questionService.updateQuestion(question);
        return "redirect:/";
    }

}
