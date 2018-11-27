package cn.tedu.task.controlller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import cn.tedu.common.util.ObjectUtil;
import cn.tedu.common.util.UUIDUtil;
import cn.tedu.task.pojo.Task;
import cn.tedu.task.pojo.User;
import cn.tedu.task.service.TaskService;

@RestController
public class TaskController {
	@Autowired
	private TaskService taskService;
	@RequestMapping("/task/sendTask")
	public String sendTask(@RequestBody String json_task){
		Task task;
		int flag = 0;
		try {
			task = ObjectUtil.mapper.readValue(json_task,Task.class);
			String taskId = ((UUIDUtil.getUUID()).substring(0,7));
			task.setTaskId(taskId);
			if(!(task.getTaskEndTime().before(new Date(System.currentTimeMillis())))){
				task.setTaskCreateTime(new Date(System.currentTimeMillis()));
			}else{
				return "您发布任务的截止时间已经过了";
			}
			flag = taskService.insertTask(task);
			if(flag==1){
				return "任务发布成功";
			}else{
				return "任务发布失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	@RequestMapping("/task/showTasks/{userId}/{status}")
	public 	String showTasks(@PathVariable("userId") String userId,@PathVariable String status,String callback){
		List<Task> list = taskService.queryTasks(userId,status);
		String json = null;
		try {
			json = ObjectUtil.mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if(callback!=null){
			return callback+"("+json+")";
		}else{
			return json;
		}
	}
	
	@RequestMapping("/task/updateTask")
	public String updateTask(@RequestBody String json_task){
		Task task=null;
		try {
			task = ObjectUtil.mapper.readValue(json_task,Task.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "任务解析失败，请重试";
		} 
		if(task.getTaskStatus()!=2){
			return "您发布的任务已经被接收，若需修改任务内容，请及时和任务负责人联系";
		}
		String flag = taskService.updateTask(task);
		return flag;
	}
	@RequestMapping("/task/deleteTask/{taskId}")
	public int deleteTask(@PathVariable String taskId){
		int flag = taskService.deleteTask(taskId);
		return flag;
	}
	@RequestMapping("/task/queryTaskById/{taskId}")
	public String queryTaskById(@PathVariable String taskId,String callback){
		Task task = taskService.queryTaskById(taskId);
		String json_task = null;
		try {
			json_task = ObjectUtil.mapper.writeValueAsString(task);
		} catch (JsonProcessingException e) {
			json_task="{'ErrMsg':'读取任务失败，请稍后重试'}";
			e.printStackTrace();
		}
		if(callback!=null){
			return callback+"("+json_task+")";
		}else{
			return json_task;
		}
	}
	
	@RequestMapping("/task/submitTask/{taskId}")
	public int submitTask(@PathVariable String taskId){
		Task _task = taskService.queryTaskById(taskId);
		if(_task.getTaskStatus() != 1){
			return 0;
		}
		int flag = taskService.submitTask(taskId,new Date(System.currentTimeMillis()));
		return flag;
	}
	
	@RequestMapping("/task/refuseTask/{taskId}")
	public int refuseTask(@PathVariable String taskId){
		//判断任务是否是待接收状态，若不是，则直接返回0
		Task task = taskService.queryTaskById(taskId);
		if(task.getTaskStatus() != 2){
			return 0;
		}
		int flag = taskService.refuseTask(taskId);	
		return flag;
	}
	@RequestMapping("/task/receiveTask/{taskId}")
	public int receiveTask(@PathVariable String taskId){
		Task task = taskService.queryTaskById(taskId);
		if(task.getTaskStatus() != 2){
			return 0;
		}
		int flag = taskService.receiveTask(taskId);
		return flag;
	}
	@RequestMapping("/task/queryGetTotal/{status}/{userId}")
	public int queryGetTotal(@PathVariable("status") String taskStatus,@PathVariable("userId") String userId){
		int count = taskService.queryGetTotal(userId,taskStatus);
		return count;
	}
	@RequestMapping("/task/queryPostTotal/{userId}")
	public int queryPostTotal(@PathVariable("userId") String userId){
		int count = taskService.queryPostTotal(userId);
		return count;
	}
	
	@RequestMapping("/user/getUsers/{userId}")
	public String getUsers(@PathVariable("userId") String userId) throws JsonProcessingException{
		List<User> list = taskService.queryUsers(userId);
		return ObjectUtil.mapper.writeValueAsString(list);
	}
	
	
}
