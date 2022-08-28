package gitter.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userAccount;
    private String userPassword;
    private String userName;
    private Date userBirthday;
    private String userSex;
    private String userFollowRepositories;
    private String userStars;
}
