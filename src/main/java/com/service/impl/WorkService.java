package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.model.entity.Work;
import com.mapper.WorkMapper;
import com.model.vo.MonthHour;
import com.model.vo.YearHour;
import com.service.intf.IWorkService;
import com.util.InfoUtils.Info;
import com.util.InfoUtils.InfoEnum;
import com.util.InfoUtils.InfoUtil;
import com.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WorkService implements IWorkService {
    @Autowired
    private WorkMapper workMapper;

    @Override  //添加工作量
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info addWork(String workName, Double hour, String deadline) {
        Work work = new Work();
        work.setUserId(MyUtils.getUserId());
        work.setWorkName(workName);
        work.setHour(hour);
        if (!work.setDeadline(deadline)) {
            return InfoUtil.getInfo(1, "日期格式错误");
        }

        int result = workMapper.insertWork(work);
        if (result > 0) {
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(InfoEnum.FAIL);
        }
    }

    @Override  //修改工作量
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info modifyWork(Long workId, String workName, Double hour, String deadline) {
        Work work = new Work();
        work.setWorkId(workId);
        work.setWorkName(workName);
        work.setHour(hour);
        if (!work.setDeadline(deadline)) {
            return InfoUtil.getInfo(1, "日期格式错误");
        }

        int result = workMapper.updateWork(work);
        if (result > 0) {
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(2, "工作量不存在");
        }
    }

    @Override  //设置工作量为已完成
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info finishWork(Long workId) {
        int result = workMapper.finishWork(workId);
        if (result > 0) {
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(1, "工作量不存在");
        }
    }

    @Override  //获取用户当月/某年|月的工作量详情(分页)
    public Info getWork(Integer year, Integer month, Integer page) {
        try {
            if (year != null && month == null) {
                return InfoUtil.getInfo(6, "年份为不为空时，月份不能为空");
            }
            //年份若为空则为本年
            year = (year != null) ? year : (LocalDate.now().getYear());
            //月份若为空则为本月
            month = (month != null) ? month : (LocalDate.now().getMonthValue());
            //获取总条数
            int num = workMapper.getWorkNum(year, month, MyUtils.getUserId());
            //总页数
            int totalPage = num == 0 ? 0 : (num - 1) / 10 + 1;
            if (totalPage == 0) {
                return InfoUtil.getInfo(5, "本月没有工作量");
            } else if (page == 0) {
                return InfoUtil.getInfo(3, "当前已是第一页！");
            } else if (page == totalPage + 1) {
                return InfoUtil.getInfo(4, "当前已经是最后一页！");
            } else if (!(page > 0 && page <= totalPage)) {
                return InfoUtil.getInfo(2, "该页码不存在");
            }

            List<Work> works = workMapper.getWorkInfo(year, month, page, MyUtils.getUserId());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page", page);
            jsonObject.put("totalPage", totalPage);
            jsonObject.put("num", num);
            jsonObject.put("work", works);
            return InfoUtil.getInfo(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return InfoUtil.getInfo(InfoEnum.FAIL);
        }
    }

    @Override  //删除工作量
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Info deleteWork(Long workId) {
        int result = workMapper.deleteWork(workId);
        if (result > 0) {
            return InfoUtil.getInfo(InfoEnum.SUCCESS);
        } else {
            return InfoUtil.getInfo(2, "工作量不存在");
        }
    }

    @Override  //统计页面根据总|年获取时长
    public Info getHour(Integer year) {
        List<Work> works = workMapper.getWorkHour(year, MyUtils.getUserId());
        if (works == null) {
            return InfoUtil.getInfo(InfoEnum.FAIL);
        }

        //根据年获取的情况
        if (year != null) {
            //返回数据初始化
            List<MonthHour> monthHourList = new ArrayList<>();
            for (int imonth = 1; imonth <= 12; imonth++) {
                monthHourList.add(new MonthHour(imonth));
            }
            //遍历所有工作量
            for (Work work : works) {
                LocalDate startTime = work.getStartTime();
                //获取该工作量的月份
                int month = startTime.getMonthValue();
                //添加时长
                monthHourList.get(month - 1).addHour(work.getHour());
            }
            //配置返回数据
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("workload", monthHourList);
            return InfoUtil.getInfo(jsonObject);
        } else {  //根据总获取的情况
            //获取最早的一年
            int earlyYear = works.get(works.size() - 1).getStartTime().getYear();
            //获取当前年份
            int nowYear = LocalDate.now().getYear();
            //初始化返回数据
            List<YearHour> yearHourList = new ArrayList<>();
            for (int iyear = earlyYear; iyear <= nowYear; iyear++) {
                yearHourList.add(new YearHour(iyear));
            }
            //遍历所有工作量
            for (Work work : works) {
                LocalDate startTime = work.getStartTime();
                //获取该工作量的年份
                int yearOfWork = startTime.getYear();
                //添加时长
                yearHourList.get(yearOfWork - earlyYear).addHour(work.getHour());
            }
            //配置返回数据
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("workload", yearHourList);
            return InfoUtil.getInfo(jsonObject);
        }
    }
}
