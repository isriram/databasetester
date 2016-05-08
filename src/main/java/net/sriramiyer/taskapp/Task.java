package net.sriramiyer.taskapp;

class Task {
  private int id;
  private String taskName;
  private boolean completedStatus;

  Task(int id, String taskName, boolean completedStatus) {
    this.id = id;
    this.taskName = taskName;
    this.completedStatus = completedStatus;
  }

  Task(String taskName) {
    this(-1, taskName, false);
  }

  void addTaskToDatabase() {
    TaskDBConnector taskDBConnector = new TaskDBConnector();
    taskDBConnector.addTask(this);
  }

  void updateTaskInDatabase() {
    if (this.id != -1) {                                     // Task updates only if it's not a new task
      TaskDBConnector taskDBConnector = new TaskDBConnector();  // A new task has the id of -1
      taskDBConnector.updateTask(this);                     // If it *is* -1, you add the task instead of updating it
    } else {
      this.addTaskToDatabase();
    }
  }

  void deleteTaskFromDatabase() {
    if (this.id != -1) {
      TaskDBConnector taskDBConnector = new TaskDBConnector();
      taskDBConnector.deleteTaskByID(this);
    }
  }

  boolean getCompletedStatus() {
    return this.completedStatus;
  }

  void setCompletedStatus(boolean completedStatus) {
    this.completedStatus = completedStatus;
  }

  String getTaskName() {
    return this.taskName;
  }

  void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  int getId() {
    return this.id;
  }
}
