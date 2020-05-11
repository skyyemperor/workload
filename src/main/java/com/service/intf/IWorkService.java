package com.service.intf;

import com.model.entity.Work;
import com.util.InfoUtils.Info;

public interface IWorkService {

    //添加工作量
    Info addWork(String workName,Double hour,String deadline);

    //修改工作量
    Info modifyWork(Long workId, String workName, Double hour, String deadline);

    //设置工作量为已完成
    Info finishWork(Long workId);

    //获取用户当月/某年|月的工作量详情(分页)
    Info getWork(Integer year, Integer month, Integer page);

    //删除工作量
    Info deleteWork(Long workId);

    //统计页面根据总|年获取时长
    Info getHour(Integer year);

}
