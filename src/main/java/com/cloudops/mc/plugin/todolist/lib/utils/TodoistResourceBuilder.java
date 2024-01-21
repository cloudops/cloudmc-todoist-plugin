package com.cloudops.mc.plugin.todolist.lib.utils;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.utils;
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.todolist.Credentials;
import com.cloudops.mc.plugin.todolist.task.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class TodoistResourceBuilder {

   // Notice that the return type is changed to be `List<Task>`
   public static List<Task> tasks(JsonArray tasksArray) {
      List<Task> taskList = new ArrayList<>();
      for (JsonElement taskItem : tasksArray) {
         JsonObject taskObject = taskItem.getAsJsonObject();
         String id = taskObject.get(Credentials.ID).getAsString();
         String name = taskObject.get(Credentials.CONTENT).getAsString();
         String projectId = taskObject.get(Credentials.PROJECT_ID).getAsString();
         String dateAdded = taskObject.get(Credentials.ADDED_AT).getAsString();

         Task newTask = new Task.Builder(id)
                 .withName(name)
                 .withProjectId(projectId)
                 .withDateAdded(dateAdded)
                 .build();

         JsonObject due = null;
         if (taskObject.get(Credentials.DUE_DATE) != null && !taskObject.get(Credentials.DUE_DATE).equals(JsonNull.INSTANCE)) {
            due = taskObject.getAsJsonObject(Credentials.DUE_DATE);
         }

         if (due != null) {
            String date = due.get(Credentials.DATE).getAsString();
            String dateString = due.get(Credentials.DATE_STRING).getAsString();
            boolean isRecurring = due.get(Credentials.DATE_IS_RECURRING).getAsBoolean();
            newTask.setDue(date);
            newTask.setDateString(dateString);
            newTask.setRecurring(isRecurring);
         }
         taskList.add(newTask);
      }
      return taskList;
   }

   public static List<? extends ServiceEntity> projects(JsonArray projectsArray) {
      // TODO:: Implement method to return a list of Todoist projects
      return Collections.emptyList();
   }
}
