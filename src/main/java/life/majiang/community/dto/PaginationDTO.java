package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xue
 * @date 2020/3/17 - 10:44 上午
 */
@Data
public class PaginationDTO {
    //里面就是所有的问题
    private List<QuestionDTO> questions;
    //页面相关箭头
    private Boolean showPrevious;
    private Boolean showFirsPage;
    private Boolean showNext;
    private Boolean showEndPage;
    //当前页 只有一个
    private Integer page;
    //展示的12345页列表 注意数组初始化
    private List<Integer> pages = new ArrayList<Integer>();
    private Integer totalPage;


    public void setPagination(Integer totalPage, Integer page) {
        //传入过来到直接赋值 判断交给调用者
        this.totalPage = totalPage;


        //让该对象的page=页面传过来的page 可以方便页面判断是否高亮
        this.page = page;

        //展示页面列表1234567 原则是只展示当前页号的前后各3个没有就不展示
        //首先加入当前页号 然后判断当前页号的前后
        pages.add(page);
        //只操作3次 所有就是循环3次
        for (int i = 1; i <= 3; i++) {
            //这里是i控制当前页号的前后第i个号 所以离当前越远第存到0 就是顺序读取了
            //判断当前页号的前一个 也就是说当前页号比它前一个大 就加入到列表中 每次往列表第一个元素加
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            //判断当前页号后面一个是否小于总页号 是的话就加入列表
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //是否显示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        //是否显示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        //是否显示跳转到第一页
        if (pages.contains(1)) {
            showFirsPage = false;
        } else {
            showFirsPage = true;
        }

        //是否显示跳转到最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
