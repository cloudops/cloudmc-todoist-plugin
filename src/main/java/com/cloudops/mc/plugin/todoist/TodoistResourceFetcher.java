package com.cloudops.mc.plugin.todoist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cloudops.mc.plugin.todoist.project.Project;
import com.cloudops.mc.plugin.todoist.task.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TodoistResourceFetcher {

   public static List<Task> tasks(JsonArray tasksArray) {
      List<Task> taskList = new ArrayList<>();
      for (JsonElement taskItem : tasksArray) {
         try {
            JsonObject taskObject = taskItem.getAsJsonObject();
            String id = taskObject.get(Credentials.ID).getAsString();
            String name = taskObject.get(Credentials.CONTENT).getAsString();
            String projectId = taskObject.get(Credentials.PROJECT_ID).getAsString();

            Task newTask = new Task();
            newTask.setId(id);
            newTask.setName(name);
            newTask.setProjectId(projectId);

            JsonObject due = taskObject.getAsJsonObject(Credentials.DUE_DATE);
            if (due != null) {
               String date = due.get(Credentials.DATE).getAsString();
               String dateString = due.get(Credentials.DATE_STRING).getAsString();
               boolean isRecurring = due.get(Credentials.DATE_IS_RECURRING).getAsBoolean();
               Date dueDate = new SimpleDateFormat("YYYY-MM-DD").parse(date);
               newTask.setDueDate(dueDate);
               newTask.setDateString(dateString);
               newTask.setRecurring(isRecurring);
            }
            taskList.add(newTask);
         } catch (ParseException e) {
            e.printStackTrace();
         }
      }
      return taskList;
   }

   public static List<Project> projects(JsonArray projectsArray) {
      List<Project> projectsList = new ArrayList<>();
      for (JsonElement projectItem : projectsArray) {
         JsonObject projectObject = projectItem.getAsJsonObject();
         String id = projectObject.get(Credentials.ID).getAsString();
         String name = projectObject.get(Credentials.CONTENT).getAsString();
         String parentId = projectObject.get(Credentials.PARENT_ID).getAsString();
         boolean shared = projectObject.get(Credentials.SHARED).getAsBoolean();
         boolean inboxProject = projectObject.get(Credentials.INDEX_PROJECT).getAsBoolean();
         boolean deleted = projectObject.get(Credentials.DELETED).getAsBoolean();

         Project project = new Project();
         project.setId(id);
         project.setName(name);
         project.setParentId(parentId);
         project.setShared(shared);
         project.setInboxProject(inboxProject);
         project.setDeleted(deleted);
         projectsList.add(project);
      }
      return projectsList;
   }


}
