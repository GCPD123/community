package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Xue
 * @date 2020/3/13 - 9:08 下午
 */
@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "search",required = false) String search) {

        //service层的来源 当一个类需要组装两个部分时 question+user 就需要用到中间层来组装 现在还加上分页

        PaginationDTO pagination = questionService.list(search,page,size);
        //不仅带有每个问题的信息 还有用户的所有信息
        model.addAttribute("pagination", pagination);
//        把搜索条件带给页面 当上下翻页的时候可以锁主条件
        model.addAttribute("search", search);

        return "index";
    }
}
