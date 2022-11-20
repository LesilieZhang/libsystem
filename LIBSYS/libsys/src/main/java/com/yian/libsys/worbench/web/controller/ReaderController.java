package com.yian.libsys.worbench.web.controller;

import com.yian.libsys.commons.contants.Contants;
import com.yian.libsys.commons.utils.DateUtils;
import com.yian.libsys.commons.utils.UUIDUtils;
import com.yian.libsys.settings.domain.User;
import com.yian.libsys.settings.service.UserService;
import com.yian.libsys.settings.web.controller.UserController;
import com.yian.libsys.worbench.domain.Reader;
import com.yian.libsys.worbench.service.ReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yian.libsys.commons.domain.ReturnObject;

/**
 * @author ZhangYiAn
 * @version 1.0.0
 * @ClassName ReaderController.java
 * @Description 读者信息管理
 * @createTime 2022年11月19日 22:45:00
 */

@Controller
public class ReaderController {

    private final static Logger logger = LoggerFactory.getLogger((ReaderController.class));

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    @RequestMapping("/workbench/reader/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询所有的用户
      List<User> userList=userService.queryAllUsers();
      //把数据保存到request中
        request.setAttribute("userList",userList);
        //请求转发到市场活动的主页面
        return "workbench/reader/index";
    }

    @RequestMapping("/workbench/reader/saveReader.do")
    @ResponseBody
    public Object saveReader(Reader reader, HttpSession session){
        logger.info("进入当前方法");
        //获取当前用户
        User user=(User) session.getAttribute(Contants.SESSION_USER);
        reader.setId(UUIDUtils.getUUID());
        reader.setCreateTime((new Date()));
        reader.setUpdateTime(new Date());
        reader.setCreateUser(user.getName());
        reader.setUpdateUser(user.getName());
        reader.setStatus(1);//正常状态
        ReturnObject returnObject=new ReturnObject();
        logger.info("读者信息=="+reader.getClassname());
        logger.info("读者信息=="+reader.getDeptname());
        logger.info("读者信息=="+reader.getIdNumber());
        logger.info("读者信息=="+reader.getName());
        logger.info("读者信息=="+reader.getSex());
        logger.info("读者信息=="+reader.getPhone());
        logger.info("读者信息=="+reader.getId());
        try{
            int ret=readerService.saveReader(reader);
            logger.info("返回信息=="+ret);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试....");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/reader/queryReaderByConditionForPage.do")
    @ResponseBody
    public Object queryReaderByConditionForPage(String name,String idNum,String dept,String className,int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("idNumber",idNum);
        map.put("deptname",dept);
        map.put("classname",className);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        List<Reader> readerList=readerService.queryReaderByConditionForPage(map);
        int totalRows=readerService.queryCountOfReaderByCondition(map);
        //根据查询结果结果，生成响应信息
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("readerList",readerList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


}