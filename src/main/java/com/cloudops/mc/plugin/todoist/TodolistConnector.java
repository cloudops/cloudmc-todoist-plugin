package com.cloudops.mc.plugin.todoist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.annotations.Plugin;
import com.cloudops.mc.plugin.sdk.connector.ConnectionTest;
import com.cloudops.mc.plugin.sdk.connector.Connector;
import com.cloudops.mc.plugin.sdk.contexts.ConnectionContext;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.plugin.Extension;
import com.cloudops.mc.plugin.sdk.ui.form.Form;
import com.cloudops.mc.plugin.sdk.ui.form.FormElement;
import com.cloudops.mc.plugin.todoist.lib.client.TodoistFetchClient;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistErrors;
import com.cloudops.mc.plugin.todoist.lib.utils.TodoistResourceType;

/**
 * TodolistConnector validates access to the todolist service
 */
@Plugin("todolist")
public class TodolistConnector implements Connector {
   private static final Logger logger = LoggerFactory.getLogger(TodolistConnector.class);

   @Override
   public List<Credential> createConnection(ConnectionContext context, Connection connection) {
      return getConnectionCredentials(connection);
   }

   @Override
   public List<Credential> updateConnection(ConnectionContext context, Connection connection) {
      return getConnectionCredentials(connection);
   }

   private List<Credential> getConnectionCredentials(Connection connection) {
      return Arrays.asList(Credential.from(Credentials.URL, connection.getParameter(Credentials.URL)));
   }
   @Override
   public ConnectionTest testConnection(Connection connection) {
      try {
         TodoistFetchClient.getFor(connection).fetch(TodoistResourceType.PROJECTS);
         return new ConnectionTest.Builder()
                 .addToContext("labelKey", "todolist.connection.success")
                 .build();

      } catch (Exception e) {
         return new ConnectionTest.Builder()
                 // include error code from the TodoistErrors class
                 .withErrorCode(TodoistErrors.CONNECTION_TEST_ERROR)
                 .addToContext("labelKey", "todolist.connection.error")
                 .build();
      }
   }

   @Override
   public List<FormElement> getParameterFormElements() {
      List<FormElement> els = new ArrayList<>();
      els.add(Form.text(Credentials.URL).label("todolist.service_configuration.parameters.url.label").required().build());

      els.add(Form.text(Credentials.TOKEN)    // --------> (2)
              .label("todolist.service_configuration.parameters.token.label")
              .description("todolist.service_configuration.parameters.token.description")
              .required()
              .build());
      return els;
   }

   @Override
   public List<String> getParameters() {
      List<String> parameters = new ArrayList<>();
      parameters.add(Credentials.URL);
      parameters.add(Credentials.TOKEN);
      return parameters;
   }

   @Override
   public List<Extension> getExtensions() {
      return Collections.emptyList();
   }

  @Override
   public String getDependentPlugin() {
      return null;
   }

}
