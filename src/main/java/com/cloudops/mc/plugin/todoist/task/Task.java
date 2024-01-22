package com.cloudops.mc.plugin.todoist.task;

import com.cloudops.mc.plugin.sdk.annotations.Entity;
import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.google.gson.JsonObject;

@Entity(value="tasks")
public class Task implements ServiceEntity {
   private String id;

   public String getName() {
      return name;
   }

   private String name;
   private String projectId;

   public String getDue() {
      return due;
   }

   private String due;
   private String dateString;
   private Boolean isRecurring;
   private String addedAt;

   private static final String TIMEZONE = "timezone";

   private static final String LANGUAGE = "lang";

   @Override
   public String getId(){
      return id;
   }
   public String getProjectId() {
      return projectId;
   }

   public void setDue(String due) {
      this.due = due;
   }

   public void setDateString(String dateString) {
      this.dateString = dateString;
   }

   public void setRecurring(Boolean recurring) {
      isRecurring = recurring;
   }

   /* Task.java */


   // converts this Task object to its JSON representation as expected by the Todoist API
   public JsonObject toJsonObject() {
      JsonObject dueDateJson = new JsonObject();

      String formattedDue = due.trim().replace(" ", "T") + ":00";
      dueDateJson.addProperty(Credentials.DATE, formattedDue);
      dueDateJson.addProperty(Credentials.DATE_STRING, dateString);
      dueDateJson.addProperty(Credentials.DATE_IS_RECURRING, isRecurring);
      dueDateJson.add(TIMEZONE, null);
      dueDateJson.addProperty(LANGUAGE, "en");

      JsonObject taskJson = new JsonObject();
      taskJson.addProperty(Credentials.CONTENT, this.name);
      taskJson.addProperty(Credentials.PROJECT_ID, this.projectId);
      taskJson.add(Credentials.DUE_DATE, dueDateJson);
      return taskJson;
   }


   public static class Builder {
      private Task task = new Task();

      public Builder(String id) {
         this.task.id = id;
      }

      public Builder withName(String name) {
         this.task.name = name;
         return this;
      }

      public Builder withProjectId(String projectId) {
         this.task.projectId = projectId;
         return this;
      }

      public Builder withDateAdded(String addedAt) {
         this.task.addedAt = addedAt;
         return this;
      }


      public Task build() {
         return this.task;
      }
   }


}
