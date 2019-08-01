package com.cloudops.mc.plugin.todoist.task;

import java.text.ParseException;
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
   private DueDate dueDate = new DueDate();

   public JsonObject toJsonObject() {
      JsonObject taskJson = new JsonObject();
      taskJson.addProperty(Credentials.CONTENT, this.name);
      taskJson.addProperty(Credentials.PROJECT_ID, this.projectId);
      taskJson.add(Credentials.DUE_DATE, this.dueDate.toJsonObject());
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

   public DueDate getDueDate() {
      return dueDate;
   }

   public void setDueDate(DueDate dueDate) {
      this.dueDate = dueDate;
   }

   public DateString fromDatePhrase(String typeStr) {
      for (DateString currString: DateString.values()) {
         if (currString.getType().equals(typeStr)) {
            return currString;
         }
      }
      return null;
   }

   public Date fromDateString(String dateString) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-DD");
      try {
         return dateFormatter.parse( dateString);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return null;
   }

   public class DueDate {
      private Date date;
      private DateString dateString;
      private Boolean isRecurring;

      JsonObject toJsonObject() {
         SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-DD");
         JsonObject dueDateJson = new JsonObject();
         dueDateJson.addProperty(Credentials.DATE, dateFormatter.format(date));
         dueDateJson.addProperty(Credentials.DATE_STRING, this.dateString.getType());
         dueDateJson.addProperty(Credentials.DATE_IS_RECURRING, this.isRecurring);
         dueDateJson.add("timezone", null);
         dueDateJson.addProperty("lang", "en");
         return dueDateJson;
      }

      public Date getDate() {
         return date;
      }

      public void setDate(Date date) {
         this.date = date;
      }

      public DateString getDateString() {
         return dateString;
      }

      public void setDateString(DateString dateString) {
         this.dateString = dateString;
      }

      public Boolean getRecurring() {
         return isRecurring;
      }

      public void setRecurring(Boolean recurring) {
         isRecurring = recurring;
      }
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

      private String getType() {
         return this.dateType;
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
