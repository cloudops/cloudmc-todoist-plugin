package com.cloudops.mc.plugin.todolist.lib.utils;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.utils;
 */

public enum TodoistAction {
   PROJECT_ADD("project_add"),
   PROJECT_DELETE("project_delete"),
   PROJECT_UPDATE("project_update"),
   SHARE_PROJECT("share_project"),
   DELETE_COLLABORATOR("delete_collaborator"),

   ADD_USER("register"),
   DELETE_USER("delete"),

   TASK_ADD("item_add"),
   TASK_DELETE("item_delete"),
   TASK_UPDATE("item_update");

   private String action;

   TodoistAction(String action) {
      this.action = action;
   }

   public String getTodoistAction() {
      return this.action;
   }
}
