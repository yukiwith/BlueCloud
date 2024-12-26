package com.example.bolgwithcontents.controller;

import com.example.bolgwithcontents.model.UserDTO;
import com.example.bolgwithcontents.service.impl.UserServiceImpl;
import com.example.bolgwithcontents.model.Result;
import com.example.bolgwithcontents.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserServiceImpl userService;


    @GetMapping
    public Result Get() {
        return new Result("200","",userService.FindAll());
    }

    @PostMapping("/login")
    public Result Login(@RequestBody UserDTO userDTO) {
        boolean flag = userService.Login(userDTO.getAccount(),userDTO.getPassword());
        if (flag) {
            return new Result("200","","Bearer " + JwtUtil.generateToken(userDTO.getAccount()));
        } else {
            return new Result("204", "用户名或密码错误", null);
        }
    }

    @PostMapping("/register")
    public Result Register(@RequestBody UserDTO userDTO){
        if (userService.Register(userDTO.getAccount(), userDTO.getPassword())) {
            return new Result("200","",null);
        }
       return new Result("204","您已经注册过了哦",null);
    }
}
