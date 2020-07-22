package cn.yangself.leigodcontrol.mapper;

import cn.yangself.leigodcontrol.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LeigodMapper {

    /**
     * 新增用户
     * @param user
     * @return
     */
    public int addUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    public int updateUser(User user);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public User selectUserByUsername(@Param("username") String username);

    /**
     * 查找所有用户信息
     * @return
     */
    public List<User> selectAllUsers();

}
