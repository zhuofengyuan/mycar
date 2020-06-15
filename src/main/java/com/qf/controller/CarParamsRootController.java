package com.qf.controller;

import com.qf.dto.DataGridResult;
import com.qf.dto.QueryDTO;
import com.qf.pojo.CarParams;
import com.qf.pojo.CarParamsRoot;
import com.qf.service.CarParamRootService;
import com.qf.service.CarParamService;
import com.qf.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/sys/paramsroot/")
public class CarParamsRootController {

    @Autowired
    private CarParamService carParamTypeService;

    @Autowired
    private CarParamRootService carParamService;

    @ResponseBody
    @RequestMapping("/list")
    public DataGridResult findCarParam(QueryDTO queryDTO){
        return carParamService.findByPage(queryDTO);
    }

    @ResponseBody
    @RequestMapping("/del")
    public R deleteCarParams(@RequestBody List<Integer> ids){
        for (Integer id : ids) {
            carParamService.delCarParams(id);
        }
        return R.ok();
    }

    //获取分类信息
    @ResponseBody
    @RequestMapping("/typeall")
    public R typeAll(){
        List<CarParams> all = carParamTypeService.findAll();
        return R.ok().put("sites",all);
    }


    @ResponseBody
    @RequestMapping("/info/{id}")
    public R findById(@PathVariable("id") Integer id){
        CarParamsRoot byId = carParamService.findById(id);
        return R.ok().put("params",byId);
    }

    @ResponseBody
    @RequestMapping("/save")
    public R addCarParams(@RequestBody CarParamsRoot carParams){
        int i = carParamService.addCarParams(carParams);
        return i>0?R.ok():R.error("添加失败");
    }

    @ResponseBody
    @RequestMapping("/update")
    public R updateCarParams(@RequestBody CarParamsRoot carParams){
        int i = carParamService.updateCarParams(carParams);
        return i>0?R.ok():R.error("修改失败");
    }


}
