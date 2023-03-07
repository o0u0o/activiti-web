package com.o0u0o.activitiweb;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part2_ProcessDefinition {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * <h2>查询流程定义</h2>
     */
    @Test
    public void getDefinitions(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for(ProcessDefinition pd : list){
            System.out.println("------流程定义--------");
            System.out.println("Name："+pd.getName());
            System.out.println("Key："+pd.getKey());
            System.out.println("ResourceName："+pd.getResourceName());
            System.out.println("DeploymentId："+pd.getDeploymentId());
            System.out.println("Version："+pd.getVersion());
        }

    }

    /**
     * <h2>删除流程定义</h2>
     */
    @Test
    public void delDefinition(){
        String pdID = "6e4bec43-bbfe-11ed-8372-66605be3c767";
        // true-连带历史数据全部删除  false-保留历史数据（实际工作中基本使用false）
        repositoryService.deleteDeployment(pdID,true);
        System.out.println("删除流程定义成功");
    }
}
