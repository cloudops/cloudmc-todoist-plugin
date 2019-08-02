package com.cloudops.mc.plugin.todoist.project;

import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;

public class Project implements ServiceEntity {
   private String id;
   private String name;
   private String parentId;
   private boolean shared;
   private boolean deleted;
   private boolean inboxProject;

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

   public String getParentId() {
      return parentId;
   }

   public void setParentId(String parentId) {
      this.parentId = parentId;
   }

   public boolean isShared() {
      return shared;
   }

   public void setShared(boolean shared) {
      this.shared = shared;
   }

   public boolean isDeleted() {
      return deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }

   public boolean isInboxProject() {
      return inboxProject;
   }

   public void setInboxProject(boolean inboxProject) {
      this.inboxProject = inboxProject;
   }
}
