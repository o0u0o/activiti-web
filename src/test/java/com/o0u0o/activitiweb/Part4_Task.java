package com.o0u0o.activitiweb;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part4_Task {

    @Autowired
    private TaskService taskService;

    /**
     * <h2>任务查询</h2>
     * 查询的是所有任务，基本上是给管理员使用
     */
    @Test
    public void getTasks(){
        List<Task> list = taskService.createTaskQuery().list();

        list.stream().forEach( tk ->{
            System.out.println("Id(任务ID)：" + tk.getId());
            System.out.println("Name(任务名)：" + tk.getName());
            System.out.println("Assignee(任务代理人)：" + tk.getAssignee());
        });

    }

    /**
     * <h2>查询我的代办任务</h2>
     * 查询自己的代办任务
     * bajie -> wukong
     */
    @Test
    public void getTasksByAssignee(){
        List<Task> list = taskService.createTaskQuery().taskAssignee("wukong").list();
        for(Task tk : list){
            System.out.println("Id(任务ID)：" + tk.getId());
            System.out.println("Name(任务名):" + tk.getName());
            System.out.println("Assignee：" + tk.getAssignee());
        }

    }

    /**
     * <h2>执行任务</h2>
     *
     */
    @Test
    public void completeTask(){
        taskService.complete("4420b29e-bcb1-11ed-bf8b-66605be3c767");
        System.out.println("完成任务");

    }

    /**
     * <h2>拾取任务</h2>
     * 什么是拾取任务呢？
     *  当设置的是多个候选人的时候，候选人会拾取任务该任务
     */
    @Test
    public void claimTask(){
        //先查询出任务
        Task task = taskService.createTaskQuery().taskId("26714aa5-bcb3-11ed-beb1-66605be3c767").singleResult();
        taskService.claim("26714aa5-bcb3-11ed-beb1-66605be3c767","bajie");
    }

    /**
     * <h2>归还与交办任务</h2>
     * 1、有些是有，我们发现拾取任务不是属于自己的，所以就有了归还任务
     * 2、比如技术部领导接任务，需要交办到开发小李
     */
    @Test
    public void setTaskAssignee(){

        String taskId = "";

        Task task = taskService.createTaskQuery().taskId("1f2a8edf-cefa-11ea-84aa-dcfb4875e032").singleResult();

        //归还候选任务（就是将执行人设置为null）
        taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032","null");

        //交办（就是设置执行人）
        taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032","wukong");
    }



}
