package cn.tedu.task.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.task.mapper.TaskMapper;
import cn.tedu.task.pojo.Task;
import cn.tedu.task.pojo.User;

@Service
public class TaskService {

	@Autowired
	private TaskMapper taskMapper;

	public int insertTask(Task task) {
		
		return taskMapper.insertTask(task);
	}

	public List<Task> queryTasks(String userId, String status) {
		Map<String,String> map = new HashMap<String,String>();
		List<Task> list = null;
		map.put("taskStatus",status);
		map.put("userId",userId);
		if("5".equals(status)){
			list = taskMapper.queryPostTasks(map);
		}else{
			list = taskMapper.queryMyTasks(map);
		}
		return list;
	}

	public String updateTask(Task task) {
		int flag = taskMapper.updateTask(task);
		if(flag==1){
			return "任务修改成功";
		}
		return "任务修改失败";
	}

	public int deleteTask(String taskId) {
		int flag = taskMapper.deleteTask(taskId);
		return flag;
	}

	public Task queryTaskById(String taskId) {
		Task task = taskMapper.queryTaskById(taskId);
		return task;
	}


	public int refuseTask(String taskId) {
		Task task = new Task();
		task.setTaskId(taskId);
		task.setTaskStatus(3);
		int flag = taskMapper.updateTaskStatus(task);
		return flag;
	}

	public int receiveTask(String taskId) {
		Task task = new Task();
		task.setTaskId(taskId);
		task.setTaskStatus(1);
		int flag = taskMapper.updateTaskStatus(task);
		return flag;
	}

	public int submitTask(String taskId, String taskFinishTime) {
		Task task = new Task();
		task.setTaskId(taskId);
		task.setTaskFinishTime(taskFinishTime);
		int flag = taskMapper.updateTaskStatusAndFinishTime(task);
		return flag;
	}

	public int queryGetTotal(String userId, String taskStatus) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", userId);
		map.put("taskStatus",taskStatus);
		int count = taskMapper.queryGetTotal(map);
		return count;
	}

	public int queryPostTotal(String userId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId",userId);
		int count = taskMapper.queryPostTotal(map);
		return count;
	}

	public List<User> queryUsers(String userId) {
		User user = new User();
		user.setUserId(userId);
		List<User> list = taskMapper.queryUsers(user);
		return list;
	}
}
