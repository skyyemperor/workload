package com.mapper;

import com.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //获取用户信息
    @Select("SELECT `id`,`user_id`,`name`,`campus`,`academy`,`department`,`position`,`flag`" +
            " FROM user WHERE `user_id` = #{userId} LIMIT 1")
    User getUserInfo(String userId);

    //插入新的用户
    @Insert("INSERT INTO user (`user_id`,`name`,`academy`) VALUES (#{userId},#{name},#{academy})")
    int insertUser(User user);

    //更新用户信息，可更新的属性为campus、department、position、flag，可为空
    @UpdateProvider(type = UserProvider.class, method = "updateUserInfo")
    int updateUserInfo(User user);

    //获取用户是否验证过的属性
    @Select("SELECT `verified` FROM user WHERE `user_id` = #{userId} LIMIT 1")
    int getUserVerified(String userId);


    //设置用户是否验证过
    @Update("UPDATE user SET `verified` = #{verified} where `user_id` = #{userId} LIMIT 1")
    int setUserVerified(Integer verified, String userId);


    /**
     * provider内部类，提供复杂的sql语句
     */
    class UserProvider {
        public String updateUserInfo(User user) {
            StringBuilder sql = new StringBuilder(" UPDATE user SET ");
            if (user.getCampus() != null) {
                sql.append(" `campus` = #{campus} , ");
            }
            if (user.getDepartment() != null) {
                sql.append(" `department` = #{department} , ");
            }
            if (user.getPosition() != null) {
                sql.append(" `position` = #{position} , ");
            }
            if (user.getFlag() != null) {
                sql.append(" `flag` = #{flag} , ");
            }
            sql.append(" WHERE `user_id` = #{userId} LIMIT 1");

            //去除最后一个“,”
            return sql.deleteCharAt(sql.lastIndexOf(",")).toString();
        }


    }
}
