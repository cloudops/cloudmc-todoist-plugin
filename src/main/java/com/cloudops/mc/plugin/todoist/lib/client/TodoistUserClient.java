package com.cloudops.mc.plugin.todoist.lib.client;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.client;
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.models.User;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistAction;
import com.google.gson.JsonObject;

/**
 * Add the correct import paths with your package names
 * import <packageName>.<pluginShortName>.lib.utils.TodoistAction;
 */

public class TodoistUserClient extends TodoistClient {

   private TodoistUserClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      super(apiContext, parameterMap, todoistApiCommand);
   }

   public static TodoistUserClient getFor(Connection connection, User user) {
      String todoistToken = connection.getParameter(Credentials.TOKEN);
      return getFor(connection, user, todoistToken);
   }

   public static TodoistUserClient getFor(Connection connection, User user, String userToken) {
      String todoistUrl = getFormattedURL(connection.getParameter(Credentials.URL) + USER);
      String fullname = user.getFirstName() + " " + user.getLastName();

      String email = user.getEmail();
      // the email provided is used as the password for Todoist with just the @ replaced into a _
      // you can use this info to log into Todoist directly and see the changes
      String password = email.replace("@", "_");

      Map<String, String> paramsMap = new HashMap<>();
      paramsMap.put(Credentials.TOKEN, userToken);
      paramsMap.put(Credentials.FULL_NAME, fullname);
      paramsMap.put(Credentials.EMAIL, email);
      paramsMap.put(Credentials.PASSWORD, password);
      return new TodoistUserClient(todoistUrl, paramsMap, null);
   }

   public List<Credential> addUser() {
      List<Credential> credentials = new ArrayList<>();
      try {
         apiContext += ("/" + TodoistAction.ADD_USER.getTodoistAction());
         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         RequestBuilder requestBuilder = RequestBuilder.get();
         fillRequestEntities(multipartBuilder, requestBuilder, credentials);
         JsonObject responseJson = executeHttpRequest(multipartBuilder, requestBuilder);

         String id = responseJson.get(Credentials.ID).getAsString();
         String token = responseJson.get(Credentials.TOKEN).getAsString();
         String websocket_url = responseJson.get(Credentials.WEBSOCKET_URL).getAsString();
         String indexProject = responseJson.get(Credentials.INBOX_PROJECT).getAsString();

         credentials.add(Credential.from(Credentials.ID, id));
         credentials.add(Credential.from(Credentials.TOKEN, token));
         credentials.add(Credential.from(Credentials.WEBSOCKET_URL, websocket_url));
         credentials.add(Credential.from(Credentials.INBOX_PROJECT, indexProject));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return credentials;
   }

   public void deleteUser(String todoistUserToken, String userSecret) {
      List<Credential> credentials = new ArrayList<>();
      try {
         apiContext += (File.separator + TodoistAction.DELETE_USER.getTodoistAction());
         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         RequestBuilder requestBuilder = RequestBuilder.get();
         fillRequestEntities(multipartBuilder, requestBuilder, credentials);

         multipartBuilder.addTextBody(Credentials.TOKEN, todoistUserToken);
         multipartBuilder.addTextBody(Credentials.CURRENT_PASSWORD, userSecret);
         requestBuilder.addParameter(Credentials.TOKEN, todoistUserToken);
         requestBuilder.addParameter(Credentials.CURRENT_PASSWORD, userSecret);
         executeHttpRequest(multipartBuilder, requestBuilder);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}