package com.cloudops.mc.plugin.todoist.lib.client;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.client;
 */

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceBuilder;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Add the correct import paths with your package names
 * import <packageName>.<pluginShortName>.lib.utils.TodoistResourceBuilder;
 * import <packageName>.<pluginShortName>.lib.utils.TodoistResourceType;
 */

public class TodoistFetchClient extends TodoistClient {

   TodoistFetchClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      super(apiContext, parameterMap, todoistApiCommand);
   }

   public static TodoistFetchClient getFor(Connection connection) {
      return (TodoistFetchClient) getClient(connection, TodoistResourceType.READ_ONLY, null);
   }

   public List<? extends ServiceEntity> fetch(TodoistResourceType resourceType) throws IOException {
      String type = resourceType.getIdentifier();
      parameterMap.put(Credentials.RESOURCE_TYPES, "[\"" + type + "\"]");
      MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
      RequestBuilder requestBuilder = RequestBuilder.get();
      fillRequestEntities(multipartBuilder, requestBuilder, null);
      JsonObject responseJson = executeHttpRequest(multipartBuilder, requestBuilder);
      JsonArray resourcesArray = responseJson.getAsJsonArray(type);

      switch (resourceType) {
         case TASKS:
            return TodoistResourceBuilder.tasks(resourcesArray);
         case PROJECTS:
            return TodoistResourceBuilder.projects(resourcesArray);
      }
      return Collections.emptyList();
   }
}