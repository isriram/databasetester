package net.sriramiyer.taskapp;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class TaskDBConnectorTest {
  @Test
  public void shouldCheckIfDBExists() {
    TaskDBConnector taskDBConnector = new TaskDBConnector();
    boolean dbExists = taskDBConnector.doesDatabaseExist();
    Assert.assertEquals(dbExists, true);
  }

  @Test
  public void shouldAddNewTask() throws Exception {
    double someTask = Math.floor(100 * Math.random());//"Go buy a laptop";
    String taskName = Double.toString(someTask);
    Task task = new Task(taskName);
    task.addTaskToDatabase();
    ArrayList<Task> taskList;
    TaskDBConnector tb = new TaskDBConnector();
    taskList = tb.getTaskByTaskName(taskName);
    String verifiedTaskName;
    for (Task t : taskList) {
      if (t.getTaskName().equals(taskName)) {
        System.out.println(t.getTaskName() + " " + t.getId());
        verifiedTaskName = t.getTaskName();
        Assert.assertEquals(verifiedTaskName, taskName);
      }
    }
  }

  @Test
  public void shouldReadTaskFromDatabaseAndUpdate() {
    String taskNameNew = "Go buy bread";
    String taskNameOld = "Go buy pastry";
    //String taskNameNew = "Go buy pastry";
    //String taskNameOld = "Go buy bread";
    int testRowID;
    Task task;
    Task task1;
    ArrayList<Task> taskList;
    TaskDBConnector tb = new TaskDBConnector();
    taskList = tb.getTaskByTaskName(taskNameOld);
    if (taskList.size() != 0) {
      task = taskList.get(0);
      task.setTaskName(taskNameNew);
      testRowID = task.getId();
      task.updateTaskInDatabase();
      task1 = tb.getTaskByID(testRowID);
      Assert.assertEquals(task.getTaskName(), task1.getTaskName());
    }
  }

  @Test
  public void shouldChangeCompletionStatusByRowID() {
    boolean readTaskCompleteStatus;
    Task task;
    Task task2;
    int testRowID = 2;
    TaskDBConnector tb = new TaskDBConnector();
    task = tb.getTaskByID(testRowID);
    readTaskCompleteStatus = task.getCompletedStatus();
    task.setCompletedStatus(!readTaskCompleteStatus);
    task.updateTaskInDatabase();
    task2 = tb.getTaskByID(testRowID);
    Assert.assertNotEquals(readTaskCompleteStatus, task2.getCompletedStatus());
  }

  @Test
  public void shouldDeleteATaskByRowID() {
    int testRowID = 17;
    TaskDBConnector tb = new TaskDBConnector();
    Task task = tb.getTaskByID(testRowID);
    task.deleteTaskFromDatabase();
  }
}