package life.majiang.community.controller;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Xue
 * @date 2020/3/13 - 9:08 下午
 */
@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;
    @Autowired
    HotTagCache hotTagCache;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "tag", required = false) String tag) {

        //service层的来源 当一个类需要组装两个部分时 question+user 就需要用到中间层来组装 现在还加上分页
// 创建查询条件
        PaginationDTO pagination = questionService.list(search, tag, page, size);
//        获得热门标签
        List<String> tags = hotTagCache.getHots();
        //不仅带有每个问题的信息 还有用户的所有信息
        model.addAttribute("pagination", pagination);
//        把搜索条件带给页面 当上下翻页的时候可以锁主条件
        model.addAttribute("search", search);
//        给到页面上
        model.addAttribute("tags", tags);
//        回显到页面上 是为了每次下一页的时候都能带上这个条件
        model.addAttribute("tag", tag);

        return "index";
    }
}
