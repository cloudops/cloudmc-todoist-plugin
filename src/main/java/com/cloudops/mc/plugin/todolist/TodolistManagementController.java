package com.cloudops.mc.plugin.todolist;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.annotations.PluginComponent;
import com.cloudops.mc.plugin.sdk.components.management.EnvironmentController;
import com.cloudops.mc.plugin.sdk.components.management.EnvironmentServiceDetail;
import com.cloudops.mc.plugin.sdk.components.management.OrganizationController;
import com.cloudops.mc.plugin.sdk.components.management.UserController;
import com.cloudops.mc.plugin.sdk.contexts.EnvironmentContext;
import com.cloudops.mc.plugin.sdk.contexts.OrganizationContext;
import com.cloudops.mc.plugin.sdk.contexts.UserContext;
import com.cloudops.mc.plugin.sdk.exception.ServiceException;
import com.cloudops.mc.plugin.sdk.models.Account;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.models.Environment;
import com.cloudops.mc.plugin.sdk.models.Organization;
import com.cloudops.mc.plugin.sdk.models.User;
import com.cloudops.mc.plugin.todolist.lib.client.TodoistProjectsClient;
import com.cloudops.mc.plugin.todolist.lib.client.TodoistUserClient;
import com.cloudops.mc.plugin.todolist.lib.utils.TodoistAction;
import com.google.gson.JsonObject;

/**
 * The TodolistManagementController handles all org, env and user management on the service
 */
@PluginComponent
public class TodolistManagementController implements OrganizationController, UserController, EnvironmentController {
   private static final Logger logger = LoggerFactory.getLogger(TodolistManagementController.class);

   @Override
   public List<Credential> createOrganization(OrganizationContext organizationContext, Organization organization) {
      // the `Connection` object carries the credentials provided via the Connector when adding a new service-connection
      // these credentials can be used by the plugin to invoke the Todoist API
      Connection connection = organizationContext.getConnection();
      // the `Organization` object carries the information about the organization to which the connection is assigned
      String orgName = organization.getName();
      // we create the JSON object for a `Project` as expected by the Todoist API for creating projects
      JsonObject newProject = new JsonObject();
      newProject.addProperty(Credentials.NAME, orgName);
      // the Todoist project-specific client is used to add a new `Project` that maps to this organization
      return TodoistProjectsClient.getFor(connection, newProject).addProject(orgName);
   }

   @Override
   public void deleteOrganization(OrganizationContext organizationContext) {
      Connection connection = organizationContext.getConnection();
      Organization organization = organizationContext.getOrganization();
      // an `Organization` account is created when a connection is assigned to an organization
      // this account holds the information specific to the mapped organization in the backend-service
      Account orgAccount = organization.getAccount();
      String orgName = organization.getName();
      deleteTodoistProject(connection, orgAccount, orgName);
   }

   private void deleteTodoistProject(Connection connection, Account account, String projectName) {
      String projectId = account.getCredential(Credentials.ID);
      // we create the JSON object for a `Project` as expected by the Todoist API for deleting projects
      JsonObject projectToDelete = new JsonObject();
      projectToDelete.addProperty(Credentials.ID, Long.parseLong(projectId));
      TodoistProjectsClient.getFor(connection, projectToDelete).deleteProject(projectName);
   }

   @Override
   public List<Credential> createUser(UserContext userContext, User user) {
      Connection connection = userContext.getConnection();
      // the Todoist user-specific client is used to add a new `User` that maps to this user
      return TodoistUserClient.getFor(connection, user).addUser();
   }

   @Override
   public void deleteUser(UserContext userContext) {
      Connection connection = userContext.getConnection();
      // a `User` account is created with the credentials returned by createUser()
      // this account holds the information specific to the mapped user in the backend-service
      Account userAccount = userContext.getUserAccount();
      User user = userContext.getUser();
      String usersTodoistToken = userAccount.getCredential(Credentials.TOKEN);
      String userSecret = userAccount.getCredential(Credentials.PASSWORD);
      // Here we use the `API Token` of the user to be deleted instead of our connection token
      TodoistUserClient.getFor(connection, user, usersTodoistToken).deleteUser(usersTodoistToken, userSecret);
   }

   @Override
   public List<Credential> createEnvironment(EnvironmentContext environmentContext, Environment environment) {
      // the `Environment` object contains any information related to the environment that is stored in CMC.
      // the `Connection` object carries the credentials provided via the Connector when adding a new service-connection;
      // these credentials can be used by the plugin to invoke the Todoist API.
      Connection connection = environmentContext.getConnection();
      Organization organization = environmentContext.getOrganization();
      // we get the ID from the Organization Account which corresponds to a parent Project in Todoist
      String parentProjectId = organization.getAccount().getCredential(Credentials.ID);
      String environmentName = environment.getName();
      // we create the JSON object for a `Project` with its parent being set to the Project that maps to our Organization
      JsonObject newProject = new JsonObject();
      newProject.addProperty(Credentials.NAME, environmentName);
      newProject.addProperty(Credentials.PARENT_ID, parentProjectId);
      // the Todoist project-specific client is used to add a new `Project` that maps to this environment
      return TodoistProjectsClient.getFor(connection, newProject).addProject(environmentName);
   }

   @Override
   public void updateEnvironment(EnvironmentContext environmentContext, Environment updatedEnvironment) {

   }

   @Override
   public void deleteEnvironment(EnvironmentContext environmentContext) {
      Connection connection = environmentContext.getConnection();
      Environment environment = environmentContext.getEnvironment();
      // an `Environment` account is created when a connection is assigned to an organization
      // this account holds the information specific to the mapped environment in the backend-service
      Account environmentAccount = environment.getAccount();
      String envName = environment.getName();
      deleteTodoistProject(connection, environmentAccount, envName);
   }

   @Override
   public List<Credential> addUserToEnvironment(EnvironmentContext environmentContext, User user) throws ServiceException {
      // invoke the common method (implemented below) with the `SHARE_PROJECT` option
      // optionally, can grant different roles on the service depending on the ServiceEnvironmentRole
      return updateProjectWithUserMembership(environmentContext, user, TodoistAction.SHARE_PROJECT);
   }

   @Override
   public void removeUserFromEnvironment(EnvironmentContext environmentContext, User user) {
      // invoke the common method (implemented below) with the `DELETE_COLLABORATOR` option
      updateProjectWithUserMembership(environmentContext, user, TodoistAction.DELETE_COLLABORATOR);
   }

   @Override
   public List<EnvironmentServiceDetail> getEnvironmentServiceDetails(EnvironmentContext environmentContext) {
      return null;
   }

   private List<Credential> updateProjectWithUserMembership(EnvironmentContext environmentContext, User user, TodoistAction action) {
      Connection connection = environmentContext.getConnection();
      Environment environment = environmentContext.getEnvironment();
      String parentProjectId = environment.getAccount().getCredential(Credentials.ID);
      String environmentName = environment.getName();
      // User identified by email in Todoist
      String userEmail = user.getAccount().getCredential(Credentials.EMAIL);

      JsonObject updatedProject = new JsonObject();
      updatedProject.addProperty(Credentials.PROJECT_ID, parentProjectId);
      updatedProject.addProperty(Credentials.EMAIL, userEmail);
      TodoistProjectsClient client = TodoistProjectsClient.getFor(connection, updatedProject);
      List<Credential> envUserCredentials = new ArrayList<>();
      switch (action) {
         case SHARE_PROJECT:
            envUserCredentials = client.shareProject(environmentName);
            envUserCredentials.add(Credential.from(Credentials.EMAIL, userEmail));
            break;
         case DELETE_COLLABORATOR:
            client.unshareProject(environmentName);
            break;
      }
      return envUserCredentials;
   }
}
