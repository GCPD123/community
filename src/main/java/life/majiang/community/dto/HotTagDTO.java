package life.majiang.community.dto;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/4/3 - 7:56 下午
 */
@Data
public class HotTagDTO implements  Comparable{
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
//        让小的在前面
        return this.getPriority() - ((HotTagDTO)o).getPriority();
    }
}
