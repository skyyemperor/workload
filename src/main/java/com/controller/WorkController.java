package com.controller;

import com.model.entity.Work;
import com.service.impl.WorkService;
import com.util.InfoUtils.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/workload")
public class WorkController {
    @Autowired
    private WorkService workService;

    //添加工作量
    @PostMapping("/addWork")
    public Info addWork(
            @RequestParam String workName,
            @RequestParam Double hour,
            @RequestParam(required = false) String deadline) {
        return workService.addWork(workName, hour, deadline);
    }


    //修改工作量
    @PostMapping("/modifyWork")
    public Info modifyWork(
            @RequestParam Long workId,
            @RequestParam(required = false) String workName,
            @RequestParam(required = false) Double hour,
            @RequestParam(required = false) String deadline) {
        return workService.modifyWork(workId, workName, hour, deadline);
    }


    //设置工作已完成
    @PostMapping("/finishWork")
    public Info finishWork(@RequestParam Long workId) {
        return workService.finishWork(workId);
    }

    //删除工作量
    @PostMapping("/deleteWork")
    public Info deleteWork(@RequestParam Long workId) {
        return workService.deleteWork(workId);
    }

    //获取用户当月/某年|月的工作量详情(分页)
    @PostMapping("/getWork")
    public Info getWork(@RequestParam(required = false) Integer year,
                        @RequestParam(required = false) Integer month,
                        @RequestParam Integer page) {
        return workService.getWork(year, month, page);
    }


    //统计页面根据总/年获取时长
    @GetMapping(value = "/getHour/{year}")
    public Info getHour(@PathVariable Integer year) {
        return workService.getHour(year);
    }

    //统计页面根据年获取时长
    @PostMapping("/getHour")
    public Info getHour2(@RequestParam(required = false) Integer year) {
        return workService.getHour(year);
    }
}
