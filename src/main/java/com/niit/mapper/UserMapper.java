package com.niit.mapper;

import com.niit.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO users(username, password, real_name, gender, id_card, phone, province, city, role) " +
            "VALUES(#{username}, #{password}, #{realName}, #{gender}, #{idCard}, #{phone}, #{province}, #{city}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int checkUsernameExists(@Param("username") String username);

    /**
     * 检查手机号是否存在
     * @param phone 手机号
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    int checkPhoneExists(@Param("phone") String phone);
}