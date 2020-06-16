package com.qf.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.dto.UserDTO;
import com.qf.pojo.Cart;
import com.qf.pojo.SysUser;
import com.qf.service.CartService;
import com.qf.service.SysUserService;
import com.qf.utils.MD5Utils;
import com.qf.utils.R;
import com.qf.utils.ShiroUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DefaultKaptcha kaptcha;

    @RequestMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response){
        // 缓存设置-设置不缓存（可选操作）
        response.setHeader("Cache-Control","no-store, no-cache");
        // 设置响应内容
        response.setContentType("image/jpg");
        //生成验证码
        String text = kaptcha.createText();//文本
        //生成图片
        BufferedImage image = kaptcha.createImage(text);
        //验证码存储到shiro的 session
        ShiroUtils.setKaptcha(text);
        try {
            //返回到页面
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"jpg",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Autowired
    CartService cartService;
    @RequestMapping("/sys/login")
    @ResponseBody
    public R login(@RequestBody UserDTO userDTO){
        //比对验证码
        String serverKaptcha = ShiroUtils.getKaptcha();
        if(!serverKaptcha.equalsIgnoreCase(userDTO.getCaptcha())){
            return R.error("验证码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        String newPass = MD5Utils.md5(userDTO.getPassword(),userDTO.getUsername(),1024);
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(),newPass);
        if(userDTO.isRememberMe()){
            token.setRememberMe(true);
        }
        subject.login(token);

        //初始化购物车
        SysUser u = this.sysUserService.findByUsername(userDTO.getUsername());
        Cart c = new Cart();
        c.setUserid(Integer.valueOf(u.getUserId().toString()));
        cartService.insert(c);
        //会去调用自定义的realm
        return R.ok();
    }

    @RequestMapping("/sys/user/list")
    @ResponseBody
    public DataGridResult findUser(QueryDTO queryDTO){
        return sysUserService.findUserByPage(queryDTO);
    }

    @RequestMapping("/sys/user/export")
    public void exportUser(HttpServletResponse response){
        Workbook workbook = sysUserService.exportUser();
        try {
            //设置响应头
            response.setContentType("application/octet-stream");//所有文件都支持
            String fileName = "员工信息.xls";
            fileName = URLEncoder.encode(fileName,"utf-8");
            response.setHeader("content-disposition","attachment;filename="+fileName);
            //文件下载
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/logout")
    public String logout(){
        ShiroUtils.logout();
        return "redirect:login.html";
    }

    @RequestMapping("/sys/user/info")
    @ResponseBody
    public R userinfo(){
        //可以从shiro中获取
        SysUser userEntity = ShiroUtils.getUserEntity();
        return R.ok().put("user",userEntity);
    }

    @RequestMapping("/user/index")
    public String userIndex(){
        return "redirect:/user/index.html";
    }
    @RequestMapping("/user/bcar")
    public String bcar(){
        return "redirect:/user/detail.html";
    }
    @RequestMapping("/user/settlement")
    public String settlement(){
        return "redirect:/user/settlement.html";
    }
    @RequestMapping("/user/order")
    public String order(){
        return "redirect:/user/my_orders.html";
    }
    @RequestMapping("/user/youhui")
    public String youhui(){
        return "redirect:/user/youhui.html";
    }
    @RequestMapping("/user/qingbao")
    public String qingbao(){
        return "redirect:/user/qingbao.html";
    }
    @RequestMapping("/user/logins")
    public String logins(){
        return "redirect:/user/login.html";
    }
    @RequestMapping("/user/login")
    @ResponseBody
    public void login(String userName,String password,HttpServletResponse response){
        Map map=new HashMap();
        Subject subject = SecurityUtils.getSubject();
        String newPass = MD5Utils.md5(password,userName,1024);
        UsernamePasswordToken token = new UsernamePasswordToken(userName,newPass);
        try {
            subject.login(token);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            map.put("result","ok");
        } catch (AuthenticationException e) {
            map.put("result","no");
            e.printStackTrace();

        }

        try {
            response.getWriter().append(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/user/personal")
    public String personal(){
        return "redirect:/user/personal.html";
    }
    @RequestMapping("/user/userInfo")
    @ResponseBody
    public void userInfo(String userName,HttpServletResponse response){
      SysUser user=sysUserService.findByUsername(userName);
      System.out.println(JSON.toJSONString(user));
        try {
            response.getWriter().append(JSON.toJSONString(user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/user/updatePass")
    @ResponseBody
    public void updatePass(String userName,String password,HttpServletResponse response){
        String newPass = MD5Utils.md5(password,userName,1024);
        int i=sysUserService.updatePassword(userName,newPass);
        String s="ok";
        if (i==0){
            s="no";
        }
        try {
            response.getWriter().append(JSON.toJSONString(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/user/updatePersonal")
    @ResponseBody
    public void updatePersonal(String address,String phone,String email,String id,HttpServletResponse response){
        int i=sysUserService.updatePersonal(address,phone,email,id);
        String s="ok";
        if (i==0){
            s="no";
        }
        try {
            response.getWriter().append(JSON.toJSONString(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/user/regist")
    @ResponseBody
    public void regist(String userName,String password,String email,String phone,HttpServletResponse response){
        SysUser user=new SysUser();
        user.setUsername(userName);
        String newPass = MD5Utils.md5(password,userName,1024);
        user.setPassword(newPass);
        user.setMobile(phone);
        user.setEmail(email);
        user.setCreateTime(new Date());
        user.setStatus(new Byte("1"));
        int i=sysUserService.insert(user);
        String s="ok";
        if (i==0){
            s="no";
        }
        try {
            response.getWriter().append(JSON.toJSONString(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/user/queryUserName")
    @ResponseBody
    public void queryUserName(String userName,HttpServletResponse response){
        SysUser user=sysUserService.findByUsername(userName);
        String s="ok";
        if (!StringUtils.isEmpty(user)){
            s="no";
        }
        try {
            response.getWriter().append(JSON.toJSONString(s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
