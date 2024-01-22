package com.cloudops.mc.plugin.todoist.lib.utils;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.utils;
 */

public enum TodoistResourceType {
   READ_ONLY("read_only"),
   PROJECTS("projects"),
   TASKS("items");

   private String resourceType;

   TodoistResourceType(String resourceType) {
      this.resourceType = resourceType;
   }

   public String getIdentifier() {
      return resourceType;
   }
}
