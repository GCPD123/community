package life.majiang.community.cache;

import life.majiang.community.dto.HotTagDTO;
import lombok.Data;
import org.attoparser.dom.INestableNode;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Xue
 * @date 2020/4/3 - 7:18 下午
 */
@Component
@Data
//这是一个单利模式成员变量只会初始化一次 后面再操作都是接着 操作这些属性，所以 定时任务的时候 需要有中间变量存储 然后统一赋值给成员变量 不然会有重复的值出现
public class HotTagCache {
    //    用来存储热门标签
    private Map<String, Integer> tags = new HashMap<>();
//排序好了的标签名称
    private List<String> hots = new ArrayList<>();

//    排序
    public void updateTags(Map<String, Integer> tags) {
        int max = 3;
//        java中的优先队列 用来实现top n算法 初始化队列的大小存放三个元素
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);
//        使用小顶堆实现 堆就是一个完全二叉树
        tags.forEach(
//                遍历传入的每一个tag 然后赋值到DTO里面
                (name, priority) -> {
                    HotTagDTO hotTagDTO = new HotTagDTO();
                    hotTagDTO.setName(name);
                    hotTagDTO.setPriority(priority);
                    if (priorityQueue.size() < 3) {
//                        存入到优先队列里面
                        priorityQueue.add(hotTagDTO);
                    } else {
//                        拿到最小的元素
                        HotTagDTO minHot = priorityQueue.peek();
//                        如果当前传入的这个标签比最小的还大  也就是说热度更高 我们重写了compare方法 比的是当前的减去传入的
                        if (hotTagDTO.compareTo(minHot) > 0) {
//                            把最小的元素移除队列 永远移除最小的 那么剩下的就是最大的3个
                            priorityQueue.poll();
//                            把我们的大的放队列里面
                            priorityQueue.add(hotTagDTO);
                        }
                    }
                }
        );
//    新创建一个数组 是为了不会重复放原来的hots里面赋值
        ArrayList<String> sortedTags = new ArrayList<>();
//          拿出最小的
//        放到列表里面去
//        上面只移除并存入到列表中最小的 还有2个 都要取出来 一直到poll为空为止

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null){
//            把最热的放到第一个元素上
            sortedTags.add(0,poll.getName());
//            继续取出来 并放入
            poll = priorityQueue.poll();

        }
//        这样就不会有重复的数据出现了 因为每一次进来都是新的sortedTags
        hots = sortedTags;
//        System.out.println(hots);
    }

}
