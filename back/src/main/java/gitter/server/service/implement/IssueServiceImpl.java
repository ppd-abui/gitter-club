package gitter.server.service.implement;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import gitter.server.mapper.IssueMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitter.server.entity.Issue;
import gitter.server.service.IssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper,Issue>  implements  IssueService{
    @Resource
    IssueMapper issueMapper;
    @Override
    public boolean createIssue(Issue issue){
        try {
            //数据库中生成仓库记录
            issueMapper.insert(issue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Issue selectByIssueTitle(Issue issue)
    {
        return issueMapper.selectOne(Wrappers.<Issue>lambdaQuery()
            .eq(Issue::getIssueTitle,issue.getIssueTitle())
            .eq(Issue::getIssueContent,issue.getIssueContent()));
    }
}
