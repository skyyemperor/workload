package com.mapper;

import com.model.entity.Work;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WorkMapper {
    //插入新的工作量
    @InsertProvider(type = WorkProvider.class, method = "insertWork")
    int insertWork(Work work);

    //更新工作量，workName,hour,deadline可更新
    @UpdateProvider(type = WorkProvider.class, method = "updateWork")
    int updateWork(Work work);

    //设置工作量已完成
    @Update(" UPDATE work SET `finished` = 1 WHERE `work_id` = #{workId} ")
    int finishWork(Long workId);

    //获取用户当月/某年|月的工作量详情(分页)
    @Select(" SELECT * FROM work WHERE date_format(`start_time`,'%Y') = #{year} AND date_format(`start_time`,'%m') = #{month} " +
            " AND `user_id` = #{userId} ORDER BY `start_time` DESC LIMIT ${(page-1)*10} , 10 ")
    List<Work> getWorkInfo(Integer year, Integer month, Integer page, String userId);

    //获取用户当月/某年|月的工作量总数
    @Select(" SELECT count(`work_id`) FROM work WHERE date_format(`start_time`,'%Y') = #{year} AND date_format(`start_time`,'%m') = #{month} " +
            " AND `user_id` = #{userId} ")
    int getWorkNum(Integer year, Integer month,String  userId);


    //删除工作量
    @Delete(" DELETE FROM work WHERE `work_id` = #{workId} ")
    int deleteWork(Long workId);

    //通过总|年获取时长
    @SelectProvider(type = WorkProvider.class, method = "getWorkHour")
    List<Work> getWorkHour(Integer year,String userId);


    class WorkProvider {
        public String insertWork(Work work) {
            StringBuilder sql = new StringBuilder(" INSERT INTO work ");
            //若无ddl，则工作默认已完成
            if (work.getDeadline() == null) {
                sql.append(" (`user_id`,`work_name`,`hour`,`start_time`,`finished`) VALUES ");
                sql.append(" (#{userId},#{workName},#{hour},now(),1) ");
            } else { //若传入ddl，则工作为未完成，需要手动点击完成/finishWork
                sql.append(" (`user_id`,`work_name`,`hour`,`start_time`,`deadline`,`finished`) VALUES ");
                sql.append(" (#{userId},#{workName},#{hour},now(),#{deadline},0) ");
            }
            return sql.toString();
        }

        public String updateWork(Work work) {
            StringBuilder sql = new StringBuilder(" UPDATE work SET ");
            if (work.getWorkName() != null) {
                sql.append(" `work_name` = #{workName} , ");
            }
            if (work.getHour() != null) {
                sql.append(" `hour` = #{hour} , ");
            }
            if (work.getDeadline() != null) {
                sql.append(" `deadline` = #{deadline} , ");
            }
            sql.append(" WHERE `work_id` = #{workId} LIMIT 1");

            return sql.deleteCharAt(sql.lastIndexOf(",")).toString();
        }

        public String getWorkHour(Integer year) {
            String sql = " SELECT `hour`,`start_time` FROM work WHERE `user_id` = #{userId} ";
            if (year != null) {
                sql += " AND date_format(`start_time`,'%Y') = #{year} ";
            }
            sql += " ORDER BY `start_time` DESC ";
            return sql;
        }
    }

}
