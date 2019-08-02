package com.cloudops.mc.plugin.todoist.utils;

public enum TodoistResources {
   PROJECTS("projects"),
   TASKS("items");

   private String resourceType;

   TodoistResources(String resourceType) {
      this.resourceType = resourceType;
   }

   public String getResourceType() {
      return resourceType;
   }
}
