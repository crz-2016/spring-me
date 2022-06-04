package com.app.demo.controller;

import com.app.api.OpenFeignApi;
import com.app.demo.calc.InsertUserToDb;
import com.app.demo.mail.Email;
import com.app.service.UserService;
import com.app.user.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * 描述：<br>
 * 版权：Copyright (c) 2011-2019<br>
 * 公司：北京活力天汇<br>
 * 作者：曹孝欢<br>
 * 版本：1.0<br>
 * 创建日期：2022/3/6<br>
 */
@RestController
public class DemoController {

//    @Autowired
    Email email;
    @RequestMapping("/calc")
    public void calc(){
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        InsertUserToDb task = new InsertUserToDb(userService,0, 999);
        Integer invoke = forkJoinPool.invoke(task);
        System.out.println(invoke);
    }


    @Resource
    OpenFeignApi openFeignApi;
    @Resource
    UserService  userService;

    @GetMapping("/index")
    public String index(){
        String message = openFeignApi.getRemoteApi("hello");
        System.out.println("message:"+message);
        return message;
    }

    @PostMapping("/findUserByTime")
    public SysUser queryByCodition(@RequestBody SysUser sysUser){
        SysUser sysUser1 = userService.queryByCondition(sysUser);
        return sysUser1;
    }

    @GetMapping("/userList")
    public  List<SysUser> userList(){
        List<SysUser> list = userService.list();
        return list;
    }

    @GetMapping("/send")
    public String sendEmail(String subject, String content, String address) throws Exception {
        email.sendMail(subject, content, address);
        return "发送成功";
    }





    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response){
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=abc.txt");
        try {
            byte[] export = openFeignApi.export();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(export);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
