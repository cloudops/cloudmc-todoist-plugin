package com.cloudops.mc.plugin.todoist.lib.client;

import static com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceType.TASKS;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistAction;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceBuilder;
import com.cloudops.mc.plugin.todoist.task.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TodoistTasksClient extends TodoistClient {

   public TodoistTasksClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      super(apiContext, parameterMap, todoistApiCommand);
   }

   public static TodoistTasksClient getFor(Connection connection, JsonObject task) {
      return (TodoistTasksClient) getClient(connection, TASKS, task);
   }

   public Optional<Task> add() {
      List<Task> tasksList = actOnTask(TodoistAction.TASK_ADD);
      String projectId = todoistApiCommand.getAsJsonObject("args").get(Credentials.PROJECT_ID).getAsString();
      return tasksList.stream()
              .filter(task -> projectId.equals(task.getProjectId()))
              .findFirst();
   }

   private List<Task> actOnTask(TodoistAction action) {
      JsonArray tasksArray = execute(action, TASKS).orElse(new JsonArray());
      return TodoistResourceBuilder.tasks(tasksArray);
   }

   /* TodoistTasksClient.java */
   public Optional<Task> delete() {
      List<Task> tasksList = actOnTask(TodoistAction.TASK_DELETE);
      String taskId = todoistApiCommand.getAsJsonObject("args").get(Credentials.ID).getAsString();
      return tasksList.stream()
              .filter(task -> taskId.equals(task.getId()))
              .findFirst();
   }

}