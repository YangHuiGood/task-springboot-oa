package cn.tedu.task.controlller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.task.pojo.Task;

@RestController
public class TaskController {
	
	@RequestMapping("/task/sendTask")
	public int sendTask(Task task){
		
		return 0;
	}
}
