package com.cloudops.mc.plugin.todoist.lib.client;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.client;
 */

import static com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceType.PROJECTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistAction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Add the correct import paths with your package names
 * import <packageName>.<pluginShortName>.lib.utils.TodoistAction;
 * import <packageName>.<pluginShortName>.lib.utils.TodoistResourceType.PROJECTS;
 */

public class TodoistProjectsClient extends TodoistClient {

   private static final Logger logger = LoggerFactory.getLogger(TodoistProjectsClient.class);

   public TodoistProjectsClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      super(apiContext, parameterMap, todoistApiCommand);
   }

   public static TodoistProjectsClient getFor(Connection connection, JsonObject project) {
      return (TodoistProjectsClient) getClient(connection, PROJECTS, project);
   }

   public List<Credential> addProject(String projectName) {
      return actOnProject(TodoistAction.PROJECT_ADD, projectName);
   }

   public List<Credential> shareProject(String projectName) {
      return actOnProject(TodoistAction.SHARE_PROJECT, projectName);
   }

   public void unshareProject(String projectName) {
      actOnProject(TodoistAction.DELETE_COLLABORATOR, projectName);
   }

   public void deleteProject(String projectName) {
      actOnProject(TodoistAction.PROJECT_DELETE, projectName);
   }

   private List<Credential> actOnProject(TodoistAction action, String projectName) {
      List<Credential> credentials = new ArrayList<>();
      JsonArray projectsArray = execute(action, PROJECTS).orElse(new JsonArray());
      for (JsonElement project : projectsArray) {
         String name = project.getAsJsonObject().get(Credentials.NAME).getAsString();
         if (name.equals(projectName)) {
            String id = project.getAsJsonObject().get(Credentials.ID).getAsString();
            credentials.add(Credential.from(Credentials.ID, id));
            credentials.add(Credential.from(Credentials.NAME, name));
            logger.info("[{}] update on project [id={}, name={}]", action, id, name);
            break;
         }
      }
      return credentials;
   }
}