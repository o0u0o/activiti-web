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
            System.out.println("Id：" + tk.getId());
            System.out.println("Name：" + tk.getName());
            System.out.println("Assignee：" + tk.getAssignee());
        });

    }

    /**
     * <h2>查询我的代办任务</h2>
     * 查询自己的代办任务
     */
    @Test
    public void getTasksByAssignee(){
        List<Task> list = taskService.createTaskQuery().taskAssignee("bajie").list();
        for(Task tk : list){
            System.out.println("Id："+tk.getId());
            System.out.println("Name："+tk.getName());
            System.out.println("Assignee："+tk.getAssignee());
        }

    }

    /**
     * <h2>执行任务</h2>
     *
     */
    @Test
    public void completeTask(){
        taskService.complete("d07d6026-cef8-11ea-a5f7-dcfb4875e032");
        System.out.println("完成任务");

    }

    /**
     * <h2>拾取任务</h2>
     * 什么是拾取任务呢？
     *  当设置的是多个候选人的时候，候选人会拾取任务该任务
     */
    @Test
    public void claimTask(){
        Task task = taskService.createTaskQuery().taskId("1f2a8edf-cefa-11ea-84aa-dcfb4875e032").singleResult();
        taskService.claim("1f2a8edf-cefa-11ea-84aa-dcfb4875e032","bajie");
    }

    //归还与交办任务
    @Test
    public void setTaskAssignee(){
        Task task = taskService.createTaskQuery().taskId("1f2a8edf-cefa-11ea-84aa-dcfb4875e032").singleResult();
        taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032","null");//归还候选任务
        taskService.setAssignee("1f2a8edf-cefa-11ea-84aa-dcfb4875e032","wukong");//交办
    }



}
