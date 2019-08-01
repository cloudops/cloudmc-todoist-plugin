package com.cloudops.mc.plugin.todoist;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.models.User;
import com.cloudops.mc.plugin.todoist.utils.TodoistAction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class TodoistClient {
   private static final Logger logger = LoggerFactory.getLogger(TodoistClient.class);

   private static final String SYNC = "/sync";
   private static final String USER = "/user";

   private static final String TYPE = "type";
   private static final String COMMANDS = "commands";
   private static final int HTTP_REDIRECT = 307;

   private String apiContext;
   private Map<String, String> parameterMap;
   private JsonObject todoistApiCommand;

   private TodoistClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      this.apiContext = apiContext;
      this.parameterMap = parameterMap;
      this.todoistApiCommand = todoistApiCommand;
   }

   static TodoistClient forProject(Connection connection, JsonObject project) {
      String todoistUrl = getFormattedURL(connection.getParameter(Credentials.URL) + SYNC);
      String todoistToken = connection.getParameter(Credentials.TOKEN);

      JsonObject command = new JsonObject();
      command.addProperty("temp_id", UUID.randomUUID().toString());
      command.addProperty("uuid", UUID.randomUUID().toString());
      command.add("args", project);

      Map<String, String> paramsMap = new HashMap<>();
      paramsMap.put(Credentials.TOKEN, todoistToken);
      paramsMap.put(Credentials.RESOURCE_TYPES, "[\"" + Credentials.PROJECTS_RESOURCE + "\"]");
      return new TodoistClient(todoistUrl, paramsMap, command);
   }

   static TodoistClient forUser(Connection connection, User user) {
      String todoistUrl = getFormattedURL(connection.getParameter(Credentials.URL) + USER);
      String todoistToken = connection.getParameter(Credentials.TOKEN);
      String fullname = user.getFirstName() + " " + user.getLastName();

      String email = user.getEmail();
      String[] emailParts = email.split("@");
      email = emailParts[0] + "+" + UUID.randomUUID().toString().substring(0, 5) + "@" + emailParts[1];
      String password = UUID.randomUUID().toString();

      Map<String, String> paramsMap = new HashMap<>();
      paramsMap.put(Credentials.TOKEN, todoistToken);
      paramsMap.put(Credentials.FULL_NAME, fullname);
      paramsMap.put(Credentials.EMAIL, email);
      paramsMap.put(Credentials.PASSWORD, password);
      return new TodoistClient(todoistUrl, paramsMap, null);
   }

   List<Credential> addProject(String projectName) {
      return actOnProject(TodoistAction.PROJECT_ADD, projectName);
   }

   List<Credential> shareProject(String projectName) {
      return actOnProject(TodoistAction.SHARE_PROJECT, projectName);
   }

   void unshareProject(String projectName) {
      actOnProject(TodoistAction.DELETE_COLLABORATOR, projectName);
   }

   void deleteProject(String projectName) {
      actOnProject(TodoistAction.PROJECT_DELETE, projectName);
   }

   private List<Credential> actOnProject(TodoistAction action, String projectName) {
      List<Credential> credentials = new ArrayList<>();
      try {
         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         RequestBuilder requestBuilder = RequestBuilder.get();
         fillRequestEntities(multipartBuilder, requestBuilder, null);

         todoistApiCommand.addProperty(TYPE, action.getTodoistAction());
         String command = "[" + todoistApiCommand.toString() + "]";
         multipartBuilder.addTextBody(COMMANDS, command);
         requestBuilder.addParameter(COMMANDS, command);

         JsonObject responseJson = executeHttpRequest(multipartBuilder, requestBuilder);
         JsonArray projectsArray = responseJson.getAsJsonArray(Credentials.PROJECTS_RESOURCE);
         for (JsonElement project: projectsArray) {
            String name = project.getAsJsonObject().get(Credentials.NAME).getAsString();
            if (name.equals(projectName)) {
               String id = project.getAsJsonObject().get(Credentials.ID).getAsString();
               credentials.add(Credential.from(Credentials.ID, id));
               credentials.add(Credential.from(Credentials.NAME, name));
               logger.info("[{}] update on project [id={}, name={}]", action, id, name);
               break;
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return credentials;
   }

   List<Credential> addUser() {
      List<Credential> credentials = new ArrayList<>();
      try {
         apiContext += (File.separator + TodoistAction.ADD_USER.getTodoistAction());
         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         RequestBuilder requestBuilder = RequestBuilder.get();
         fillRequestEntities(multipartBuilder, requestBuilder, credentials);
         JsonObject responseJson = executeHttpRequest(multipartBuilder, requestBuilder);

         String id = responseJson.get(Credentials.ID).getAsString();
         String token = responseJson.get(Credentials.TOKEN).getAsString();
         String websocket_url = responseJson.get(Credentials.WEBSOCKET_URL).getAsString();
         String indexProject = responseJson.get(Credentials.INDEX_PROJECT).getAsString();

         credentials.add(Credential.from(Credentials.ID, id));
         credentials.add(Credential.from(Credentials.TOKEN, token));
         credentials.add(Credential.from(Credentials.WEBSOCKET_URL, websocket_url));
         credentials.add(Credential.from(Credentials.INDEX_PROJECT, indexProject));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return credentials;
   }

   void deleteUser(String todoistUserToken, String userSecret) {
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

   private static String getFormattedURL(String todoistUrl) {
      if (!todoistUrl.contains("http://")) {
         todoistUrl = "http://" + todoistUrl;
      }
      return todoistUrl;
   }

   private void fillRequestEntities(MultipartEntityBuilder multipartBuilder, RequestBuilder requestBuilder, List<Credential> credentials) {
      parameterMap.keySet().forEach(key -> {
         String value = parameterMap.get(key);
         multipartBuilder.addTextBody(key, value);
         requestBuilder.addParameter(key, value);
         if (credentials != null) {
            credentials.add(Credential.from(key, value));
         }
      });
   }

   private HttpPost getPostRequest(String todoistUrl, HttpEntity multipart) {
      HttpPost postRequest = new HttpPost(todoistUrl);
      postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());
      postRequest.setEntity(multipart);
      return postRequest;
   }

   private JsonObject executeHttpRequest(MultipartEntityBuilder multipartBuilder, RequestBuilder requestBuilder) throws IOException {
      HttpEntity entity = multipartBuilder.build();
      HttpPost postRequest = getPostRequest(apiContext, entity);
      CloseableHttpClient httpClient = HttpClients.createDefault();
      CloseableHttpResponse response = httpClient.execute(postRequest);
      return doRedirect(response, requestBuilder);
   }

   private JsonObject doRedirect(CloseableHttpResponse response, RequestBuilder requestBuilder) throws IOException {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      if (isRedirect(response)) {
         // get redirect url from "location" header field
         String newUrl = response.getFirstHeader(HttpHeaders.LOCATION).getValue();
         // open the new connection again
         HttpPost httpPost = new HttpPost(requestBuilder.setUri(newUrl).build().getURI());
         response = httpClient.execute(httpPost);
      }
      return parseResponse(response, "Cannot create project on Todoist service");
   }

   private boolean isRedirect(CloseableHttpResponse response) {
      StatusLine statusLine = response.getStatusLine();
      int status = statusLine.getStatusCode();
      if (status != HttpURLConnection.HTTP_OK) {
         return status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
                 || status == HttpURLConnection.HTTP_SEE_OTHER || status == HTTP_REDIRECT;
      }
      return false;
   }

   private JsonObject parseResponse(CloseableHttpResponse response, String errorMsg) throws IOException {
      String inputLine;
      BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      StringBuilder body = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
         body.append(inputLine);
      }
      in.close();
      if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
         throw new IOException(errorMsg);
      }
      JsonParser parser = new JsonParser();
      return parser.parse(body.toString()).getAsJsonObject();
   }
}
