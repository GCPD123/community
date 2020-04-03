package life.majiang.community.schdule;

import life.majiang.community.cache.HotTagCache;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Xue
 * @date 2020/4/2 - 7:23 下午
 */
@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 5000)
//    每天一点钟执行
//    @Scheduled(cron = "0 0 1, * * *" )
    public void hotTagSchedule() {
//        设置分页需求 热门标签的分页
        int offset = 0;
        int limit = 5;
        List<Question> list = new ArrayList<>();
//      临时存储的 后面赋值给缓存 所有标签的名字和优先级
        Map<String, Integer> priorities = new HashMap<>();
//        循环每一页上的问题 一开始是offset=0 可第二次就不是了 后面都是 数组中的长度固定为limit
        while (offset == 0 || list.size() == limit) {
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
//            获取这一页上的所有问题
            for (Question question : list) {
//              定义的热门标签缓存使用map

//                所有已存在的问题的所有标签
                String[] tags = StringUtils.split(question.getTag(), ",");
//              每一个tag 将每一个tag都缓存到chach里面 还有他的优先级
                for (String tag : tags) {
//                    在缓存中查找这个标签的优先级
                    Integer priority = priorities.get(tag);
                    if (priority != null) {
//                        我们自己定义的优先级计算公式
                        priorities.put(tag, priority + 5 * question.getCommentCount());
                    } else {
                        priorities.put(tag, 5 * question.getCommentCount());
                    }


                }
//                log.info("list question : {}", question.getId());
            }

//            下一页 下一个偏移从 之前的地方开始
            offset += limit;

        }
//         所有标签循环完之后 赋值给缓存
        hotTagCache.setTags(priorities);
//        查看热门标签的缓存
//        hotTagCache.getTags().forEach(
//                (k, v) -> {
//                    System.out.println(k);
//                    System.out.println(":");
//                    System.out.println(v);
//                    System.out.println();
//                }
//        );
//        将列表中的标签排序 所有标签的名字和优先级 排序 取出3个 大到小
        hotTagCache.updateTags(priorities);

//        log.info("The time is now {}", new Date());
    }
}
