package com.cloudops.mc.plugin.todoist.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

import com.cloudops.mc.plugin.sdk.annotations.Entity;
import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.google.gson.JsonObject;

@Entity(value = "tasks")
public class Task implements ServiceEntity {
   private String id;
   private String name;
   private String projectId;
   private Date dueDate;
   private String dateString;
   private Boolean isRecurring;

   private static final String TIMEZONE = "timezone";
   private static final String LANGUAGE = "lang";
   private static final String DUE_DATE_FORMAT = "YYYY-MM-DD";
   private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DUE_DATE_FORMAT);

   public JsonObject toJsonObject() {
      JsonObject dueDateJson = new JsonObject();
      dueDateJson.addProperty(Credentials.DATE, DATE_FORMATTER.format(dueDate));
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

   @Override
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getProjectId() {
      return projectId;
   }

   public void setProjectId(String projectId) {
      this.projectId = projectId;
   }

   public Date getDueDate() {
      return dueDate;
   }

   public void setDueDate(Date dueDate) {
      this.dueDate = dueDate;
   }

   public String getDateString() {
      return dateString;
   }

   public void setDateString(String dateString) {
      this.dateString = dateString;
   }

   public Boolean getRecurring() {
      return isRecurring;
   }

   public void setRecurring(Boolean recurring) {
      isRecurring = recurring;
   }

   public enum DateString {
      EVERY_DAY("every day"),
      TODAY("today"),
      TOMORROW("tomorrow"),
      NEXT_WEEK("next week"),
      NEXT_MONTH("next month");
      private String dateType;
      DateString(String dateType) {
         this.dateType = dateType;
      }
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]")
              .add("id='" + id + "'")
              .add("name='" + name + "'")
              .toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o){
         return true;
      }
      if (o == null || getClass() != o.getClass()){
         return false;
      }
      if (!super.equals(o)){
         return false;
      }
      Task instance = (Task) o;
      return  Objects.equals(id, instance.id) &&
              Objects.equals(name, instance.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), id, name);
   }
}
