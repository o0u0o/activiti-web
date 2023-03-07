package com.imooc.activitiweb.controller;


import com.imooc.activitiweb.SecurityUtil;
import com.imooc.activitiweb.mapper.ActivitiMapper;
import com.imooc.activitiweb.pojo.Act_ru_task;
import com.imooc.activitiweb.pojo.UserInfoBean;
import com.imooc.activitiweb.util.AjaxResponse;
import com.imooc.activitiweb.util.GlobalConfig;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    ActivitiMapper mapper;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {
        return "你好";
    }

    @RequestMapping(value = "/me1", method = RequestMethod.GET)
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/me2", method = RequestMethod.GET)
    public Object user(@AuthenticationPrincipal UserInfoBean UuserInfoBean){

        return UuserInfoBean.name;
    }


    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public Object gettask() {
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        securityUtil.logInAs(userName);
        try {
            Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
            return ">  Other cannot see the task: " + tasks.getTotalItems();
        } catch (Exception e) {
            return "错误：" + e;
        }
    }


    @RequestMapping(value = "/db", method = RequestMethod.GET)
    public Object getdb() {

        try {
            List<Act_ru_task> act_ru_task123 = mapper.selectName();
            return act_ru_task123.toString();
        } catch (Exception e) {
            return "错误：" + e;
        }
    }

    //启动带参数---------------测试方法-----------------------
    @GetMapping(value = "/testStartProcess")
    public AjaxResponse testStartProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                         @RequestParam("instanceName") String instanceName) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }else{
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }

            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    .withVariable("assignee", "bajie")
                    .withVariable("day", "4")
                    .withBusinessKey("自定义BusinessKey")
                    .build());
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), processInstance.getName()+"；"+processInstance.getId());
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "创建流程实例失败", e.toString());
        }
    }


    //完成待办任务带参数
    @GetMapping(value = "/testcompleteTask")
    public AjaxResponse testcompleteTask(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    .withVariable("day", "2")//执行环节设置变量
                    .build());

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "完成失败", e.toString());
        }
    }

    //获取表单
    @GetMapping(value = "/testform")
    public AjaxResponse testform(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }


            Task task = taskRuntime.task(taskID);


            String aaa=task.getProcessDefinitionId();
            String bbb=task.getFormKey();

/*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2^类型^名称^默认值^是否参数
            默认值：无、字符常量、定义过的字段ID
            是否参数：0为不是参数，1为是参数*/

            //getProcessDefinitionId、Formkey、fpType、fpID拆分可以重现表，fpID可以初始化名称，默认值，是否参数
            //注意!!!!!!!!:表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key“task.getFormKey()”当做任务key
            UserTask userTask = (UserTask)repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());
            List<FormProperty> formProperties = userTask.getFormProperties();
            for(FormProperty fp :formProperties ){
                System.out.println("："+fp.getId());
                System.out.println("："+fp.getType());
            }





            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), formProperties);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "失败", e.toString());
        }
    }

}
