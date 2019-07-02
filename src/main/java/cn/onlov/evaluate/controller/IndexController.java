package cn.onlov.evaluate.controller;

import cn.onlov.evaluate.core.dao.entities.*;
import cn.onlov.evaluate.service.*;
import cn.onlov.evaluate.util.MyStringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yangqj on 2017/4/21.
 */
@Controller
public class IndexController {


	@Resource
	private UserService cycleUserService;
	@Resource
	private CycleRoleService cycleRoleService;
	@Resource
	private CycleBaseService cycleBaseService;
    @Resource
    private CycleRoomService cycleRoomService;


    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(HttpServletRequest request, OnlovUser onlovUser, Model model){
        if ( !MyStringUtils.isNotEmpty(onlovUser.getLoginName()) || !MyStringUtils.isNotEmpty(onlovUser.getUserPwd())) {
            request.setAttribute("msg", "用户名或密码不能为空！");
            return "/login";
        }
        OnlovUser u = cycleUserService.selectByLoginName(onlovUser.getLoginName());
        if(u==null || u.getIdentityId()!= onlovUser.getIdentityId()){
            request.setAttribute("msg", "用户不存在！");
            return "/login";
        }
        UsernamePasswordToken token = null;
        try {
        	Subject subject = SecurityUtils.getSubject();
        	if(onlovUser.getUserPwd()!=null){
        		token=new UsernamePasswordToken(onlovUser.getLoginName(), onlovUser.getUserPwd());
        		subject.login(token);
        		if(onlovUser.getIdentityId()==2)//如果是学生，跳转到学生页面
        			return "redirect:/myExams/examsPage";
        		return "redirect:usersPage";
        	}
        }catch (LockedAccountException lae) {
            request.setAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
        } catch (AuthenticationException e) {
            request.setAttribute("msg", "用户或密码不正确！");
        }
        
        if(token!=null){
        	token.clear();
        }
        return "login";
        
        
    }


    @RequestMapping(value="/usersPage")
    public String usersPage(Model model){
        List<CycleBase> bases = cycleBaseService.selectAll();
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("bases",bases);
        model.addAttribute("rooms",rooms);

        return "user/users";
    }

    @RequestMapping("/rolesPage")
    public String rolesPage(){
        return "role/roles";
    }

    @RequestMapping("/permissionsPage")
    public String permissionsPage(){
        return "resources/resources";
    }
    


    @RequestMapping("/basesPage")
    public String baseStationItemsPage(Model model){
    	List<CycleBase> bases = cycleBaseService.selectAll();
    	model.addAttribute("bases", bases);
        return "/base/bases";
    }

    @RequestMapping("/roomsPage")
    public String roomStationItemsPage(Model model){
        List<CycleRoom> rooms = cycleRoomService.selectAll();
        model.addAttribute("rooms", rooms);
        return "/room/rooms";
    }


    @RequestMapping("/403")
    public String forbidden(){
        return "403";
    }


    /**
     * 评估关系表
     * @return
     */

    @RequestMapping("/evaluate/relate/page")
    public String relate(){
        return "relate/index";
    }

    /**
     * 评估分类
     * @return
     */
    @RequestMapping("/evaluate/catalog/page")
    public String catalog(){
        return "catalog/index";
    }

    /**
     * 评估项
     * @return
     */
    @RequestMapping("/evaluate/item/page")
    public String item(){
        return "item/index";
    }

    /**
     * 评估表
     * @return
     */
    @RequestMapping("/evaluate/table/page")
    public String table(){
        return "table/index";
    }

    /**
     * 创建评估
     * @return
     */

    @RequestMapping("/evaluate/createe/page")
    public String createe(){
        return "createe/index";
    }

    /**
     * 查看已评估，待评估
     * @return
     */
    @RequestMapping("/evaluate/load/page")
    public String load(){
        return "load/index";
    }

    /**
     * 汇总统计
     * @return
     */
 @RequestMapping("/evaluate/sum/page")
    public String sum(){
        return "sum/index";
    }


}
