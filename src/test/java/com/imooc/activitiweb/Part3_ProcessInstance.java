package com.imooc.activitiweb;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part3_ProcessInstance {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * <h2>初始化流程实例</h2>
     * 方法：startProcessInstanceByKey 通过key去启动流程
     * 参数：
     *  s1：
     *  s2：业务相关的key
     */
    @Test
    public void initProcessInstance(){
        //1、获取页面表单填报的内容，请假时间，请假事由，String fromData
        //2、fromData 写入业务表，返回业务表（自己业务上的表）主键ID==businessKey
        //3、目的：把业务数据与Activiti7流程数据关联
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_claim","bKey002");
        System.out.println("流程实例ID："+processInstance.getProcessDefinitionId());
    }

    /**
     * <h2>获取流程实例列表</h2>
     * 查询流程实例
     */
    @Test
    public void getProcessInstances(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for(ProcessInstance pi : list){
            System.out.println("--------流程实例------");
            //流程实例ID
            System.out.println("ProcessInstanceId：" + pi.getProcessInstanceId());
            //流程定义id
            System.out.println("ProcessDefinitionId：" + pi.getProcessDefinitionId());
            System.out.println("isEnded:" + pi.isEnded());
            //是否被挂起
            System.out.println("isSuspended：" + pi.isSuspended());

        }

    }


    /**
     * <h2>暂停(挂起)与激活流程实例</h2>
     */
    @Test
    public void activateProcessInstance(){
         runtimeService.suspendProcessInstanceById("954eeae4-bc86-11ed-808c-acde48001122");
         System.out.println("挂起流程实例");

//        runtimeService.activateProcessInstanceById("954eeae4-bc86-11ed-808c-acde48001122");
//        System.out.println("激活流程实例");
    }

    /**
     * <h2>删除流程实例</h2>
     * 参数1：实例ID
     * 参数2：删除原因
     */
    @Test
    public void delProcessInstance(){
        runtimeService.deleteProcessInstance("954eeae4-bc86-11ed-808c-acde48001122","删着玩");
        System.out.println("删除流程实例");
    }
}
