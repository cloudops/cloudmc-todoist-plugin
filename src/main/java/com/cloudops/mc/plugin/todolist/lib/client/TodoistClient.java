package com.cloudops.mc.plugin.todolist.lib.client;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.client;
 */

import static com.cloudops.mc.plugin.todolist.lib.utils.TodoistResourceType.TASKS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.todolist.Credentials;
import com.cloudops.mc.plugin.todolist.lib.utils.TodoistAction;
import com.cloudops.mc.plugin.todolist.lib.utils.TodoistResourceType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * Add the correct import paths with your package names
 * import <packageName>.<pluginShortName>.lib.utils.TodoistAction;
 * import <packageName>.<pluginShortName>.lib.utils.TodoistResourceType;
 */

abstract class TodoistClient {

   static final String USER = "/user";
   private static final String SYNC = "/sync";
   private static final String TYPE = "type";
   private static final String COMMANDS = "commands";
   private static final String TOKEN = "token";
   private static final int HTTP_REDIRECT = 307;

   String apiContext;
   Map<String, String> parameterMap;
   JsonObject todoistApiCommand;

   TodoistClient(String apiContext, Map<String, String> parameterMap, JsonObject todoistApiCommand) {
      this.apiContext = apiContext;
      this.parameterMap = parameterMap;
      this.todoistApiCommand = todoistApiCommand;
   }

   static TodoistClient getClient(
           Connection connection, TodoistResourceType type, JsonObject resource) {
      String todoistUrl = getFormattedURL(connection.getParameter(Credentials.URL) + SYNC);
      String todoistToken = connection.getParameter(Credentials.TOKEN);

      JsonObject command = null;
      if (!type.equals(TodoistResourceType.READ_ONLY)) {
         command = new JsonObject();
         command.addProperty("temp_id", UUID.randomUUID().toString());
         command.addProperty("uuid", UUID.randomUUID().toString());
         command.add("args", resource);
      }
      Map<String, String> paramsMap = new HashMap<>();
      paramsMap.put(Credentials.TOKEN, todoistToken);

      switch (type) {
         case PROJECTS:
            paramsMap.put(Credentials.RESOURCE_TYPES, "[\"" + TodoistResourceType.PROJECTS.getIdentifier() + "\"]");
            return new TodoistProjectsClient(todoistUrl, paramsMap, command);
         case TASKS:
            paramsMap.put(Credentials.RESOURCE_TYPES, "[\""+ TASKS.getIdentifier() + "\"]");
            return new TodoistTasksClient(todoistUrl, paramsMap, command);// TODO:: To be implemented
         default:
            return new TodoistFetchClient(todoistUrl, paramsMap, command);
      }
   }

   Optional<JsonArray> execute(TodoistAction action, TodoistResourceType type) {
      Optional<JsonArray> resourcesArray = Optional.empty();
      try {
         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         RequestBuilder requestBuilder = RequestBuilder.get();
         fillRequestEntities(multipartBuilder, requestBuilder, null);

         todoistApiCommand.addProperty(TYPE, action.getTodoistAction());
         String command = "[" + todoistApiCommand.toString() + "]";
         multipartBuilder.addTextBody(COMMANDS, command);
         requestBuilder.addParameter(COMMANDS, command);

         JsonObject responseJson = executeHttpRequest(multipartBuilder, requestBuilder);
         resourcesArray = Optional.of(responseJson.getAsJsonArray(type.getIdentifier()));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return resourcesArray;
   }

   static String getFormattedURL(String todoistUrl) {
      if (!todoistUrl.contains("http://")) {
         todoistUrl = "http://" + todoistUrl;
      }
      return todoistUrl;
   }

   void fillRequestEntities(MultipartEntityBuilder multipartBuilder, RequestBuilder requestBuilder, List<Credential> credentials) {
      parameterMap.keySet().forEach(key -> {
         String value = parameterMap.get(key);
         multipartBuilder.addTextBody(key, value);
         requestBuilder.addParameter(key, value);
         if (credentials != null && !Credentials.TOKEN.equals(key)) {
            credentials.add(Credential.from(key, value));
         }
      });
   }

   private HttpPost getPostRequest(String todoistUrl, HttpEntity multipart) {
      HttpPost postRequest = new HttpPost(todoistUrl);
      postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());
      postRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.parameterMap.get(TOKEN).toString());
      postRequest.setEntity(multipart);
      return postRequest;
   }

   JsonObject executeHttpRequest(MultipartEntityBuilder multipartBuilder, RequestBuilder requestBuilder) throws IOException {
      HttpEntity entity = multipartBuilder.build();
      HttpPost postRequest = getPostRequest(apiContext, entity);
      CloseableHttpClient httpClient = HttpClients.createDefault();
      CloseableHttpResponse response = httpClient.execute(postRequest);
      return doRedirect(response, requestBuilder);
   }

   private JsonObject doRedirect(CloseableHttpResponse response, RequestBuilder requestBuilder) throws IOException {
      String newUrl = "";
      CloseableHttpClient httpClient = HttpClients.createDefault();
      if (isRedirect(response)) {
         // get redirect url from "location" header field
         newUrl = response.getFirstHeader(HttpHeaders.LOCATION).getValue();
         // open the new connection again
         HttpPost httpPost = new HttpPost(requestBuilder.setUri(newUrl).build().getURI());
         httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.parameterMap.get(TOKEN).toString());
         response = httpClient.execute(httpPost);
      }
      return parseResponse(response, "Error on redirect for [" + newUrl + "]");
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
