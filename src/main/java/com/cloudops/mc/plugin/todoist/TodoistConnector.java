package com.cloudops.mc.plugin.todoist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.Plugin;
import com.cloudops.mc.plugin.sdk.connector.ConnectionTest;
import com.cloudops.mc.plugin.sdk.connector.Connector;
import com.cloudops.mc.plugin.sdk.contexts.ConnectionContext;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.todoist.utils.TodoistErrors;
import com.cloudops.mc.plugin.todoist.utils.TodoistResources;

/**
 * TodoistConnector validates access to the Swift service
 */
@Plugin("todoist")
public class TodoistConnector implements Connector {
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
      try {
         TodoistClient.forResources(connection).fetch(TodoistResources.PROJECTS);
         return new ConnectionTest.Builder()
                 .addToContext("labelKey", "todoist.connection.success")
                 .build();

      } catch (Exception e) {
         return new ConnectionTest.Builder()
                 .withErrorCode(TodoistErrors.CONNECTION_TEST_ERROR)
                 .addToContext("labelKey", "todoist.connection.error")
                 .build();
      }
   }

   @Override
   public List<String> getParameters() {
      List<String> parameters = new ArrayList<>();
      parameters.add(Credentials.URL);
      parameters.add(Credentials.TOKEN);
      return parameters;
   }
}
