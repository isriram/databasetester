package net.sriramiyer.taskapp;

class Task {
    private int id;
    private String taskName;
    private boolean completedStatus;

    public Task(int id, String taskName, boolean completedStatus) {
        this.id = id;
        this.taskName = taskName;
        this.completedStatus = completedStatus;
    }

    public Task(String taskName, boolean completedStatus) {
        this(-1, taskName, completedStatus);
    }

    public Task(String taskName) {
        this(-1, taskName, false);
    }

    public Task() {
    }

    void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    void setCompletedStatus(boolean completedStatus) {
        this.completedStatus = completedStatus;
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
        if(this.id != -1) {
            TaskDBConnector taskDBConnector = new TaskDBConnector();
            taskDBConnector.deleteTaskByID(this);
        }
    }

    boolean getCompletedStatus() {
        return this.completedStatus;
    }

    String getTaskName() {
        return this.taskName;
    }

    public int getId() {
        return this.id;
    }
}
