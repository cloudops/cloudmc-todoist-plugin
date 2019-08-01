package com.cloudops.mc.plugin.todoist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.annotations.Plugin;
import com.cloudops.mc.plugin.sdk.connector.ConnectionTest;
import com.cloudops.mc.plugin.sdk.connector.ConnectionTest.Builder;
import com.cloudops.mc.plugin.sdk.connector.Connector;
import com.cloudops.mc.plugin.sdk.contexts.ConnectionContext;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.todoist.utils.TodoistErrors;

/**
 * TodoistConnector validates access to the Swift service
 */
@Plugin("todoist")
public class TodoistConnector implements Connector {
   private static final Logger logger = LoggerFactory.getLogger(TodoistConnector.class);

   @Override
   public List<Credential> createConnection(ConnectionContext context, Connection connection) {
      return getConnectionCredentials(connection);
   }

   @Override
   public List<Credential> updateConnection(ConnectionContext context, Connection connection) {
      return getConnectionCredentials(connection);
   }

   private List<Credential> getConnectionCredentials(Connection connection) {
      return Arrays.asList(
              Credential.from(Credentials.URL, connection.getParameter(Credentials.URL)),
              Credential.from(Credentials.TOKEN, connection.getParameter(Credentials.TOKEN)));
   }

   @Override
   public ConnectionTest testConnection(Connection connection) {
      Builder test = new ConnectionTest.Builder();
      ConnectionTest connectionTest = test
              .addToContext("labelKey", "todoist.connection.success")
              .build();

      try {
         String todoistUrl = connection.getParameter(Credentials.URL) + "sync";
         if (!todoistUrl.contains("http://")) {
            todoistUrl = "http://" + todoistUrl;
         }
         String todoistToken = connection.getParameter(Credentials.TOKEN);

         CloseableHttpClient httpClient = HttpClients.createDefault();
         HttpPost postRequest = new HttpPost(todoistUrl);
         postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
         multipartBuilder.addTextBody(Credentials.TOKEN, todoistToken);
         multipartBuilder.addTextBody(Credentials.SYNC_TOKEN, "*");
         multipartBuilder.addTextBody("resource_types", "[\"all\"]");

         HttpEntity multipart = multipartBuilder.build();
         postRequest.setEntity(multipart);
         CloseableHttpResponse response = httpClient.execute(postRequest);
         StatusLine statusLine = response.getStatusLine();

         if (statusLine.getStatusCode() > 399) {
            connectionTest = new ConnectionTest.Builder()
                    .withErrorCode(TodoistErrors.CONNECTION_TEST_FAILED)
                    .addToContext("labelKey", "todoist.connection.error")
                    .build();
         }
      } catch (IOException e) {
         connectionTest = new ConnectionTest.Builder()
                 .withErrorCode(TodoistErrors.CONNECTION_TEST_ERROR)
                 .addToContext("labelKey", "todoist.connection.error")
                 .build();
      }
      return connectionTest;
   }

   @Override
   public List<String> getParameters() {
      List<String> parameters = new ArrayList<>();
      parameters.add(Credentials.URL);
      parameters.add(Credentials.TOKEN);
      return parameters;
   }
}
