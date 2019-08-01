package com.cloudops.mc.plugin.todoist;

/**
 * Credentials contains the mappings to Account credentials
 */
public final class Credentials {
   public static final String URL = "url";
   public static final String TOKEN = "token";
//   public static final String SYNC = "/sync";
//   public static final String USER_REGISTER = "/user/register";
//   public static final String USER_DELETE = "/user/delete";
   public static final String SYNC_TOKEN = "sync_token";
//   public static final String COMMANDS = "commands";
   public static final String RESOURCE_TYPES = "resource_types";
   public static final String PROJECTS_RESOURCE = "projects";
   public static final String TASKS_RESOURCE = "items";

   public static final String ID = "id";
   public static final String IDS = "ids";
   public static final String NAME = "name";
   public static final String CONTENT = "content";
   public static final String FULL_NAME = "full_name";
   public static final String EMAIL = "email";
   public static final String PASSWORD = "password";
   public static final String CURRENT_PASSWORD = "current_password";
   public static final String WEBSOCKET_URL = "websocket_url";
   public static final String INDEX_PROJECT = "inbox_project";
   public static final String PARENT_ID = "parent_id";
   public static final String PROJECT_ID = "project_id";
   public static final String DUE_DATE = "due";
   public static final String DATE = "date";
   public static final String DATE_STRING = "string";
   public static final String DATE_IS_RECURRING = "is_recurring";

   private Credentials(){
      //static class
   }
}
