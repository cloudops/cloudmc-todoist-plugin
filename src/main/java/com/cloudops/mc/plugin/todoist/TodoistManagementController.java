package com.cloudops.mc.plugin.todoist;

import java.util.ArrayList;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.PluginComponent;
import com.cloudops.mc.plugin.sdk.components.management.EnvironmentController;
import com.cloudops.mc.plugin.sdk.components.management.EnvironmentServiceDetail;
import com.cloudops.mc.plugin.sdk.components.management.OrganizationController;
import com.cloudops.mc.plugin.sdk.components.management.UserController;
import com.cloudops.mc.plugin.sdk.contexts.EnvironmentContext;
import com.cloudops.mc.plugin.sdk.contexts.OrganizationContext;
import com.cloudops.mc.plugin.sdk.contexts.UserContext;
import com.cloudops.mc.plugin.sdk.models.Account;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.models.Environment;
import com.cloudops.mc.plugin.sdk.models.Organization;
import com.cloudops.mc.plugin.sdk.models.User;
import com.cloudops.mc.plugin.todoist.utils.TodoistAction;
import com.google.gson.JsonObject;

/**
 * The TodoistManagementController handles all org, env and user management on the service
 */
@PluginComponent
public class TodoistManagementController implements OrganizationController, UserController, EnvironmentController {

   @Override
   public List<Credential> createOrganization(OrganizationContext organizationContext, Organization organization) {
      Connection connection = organizationContext.getConnection();
      String orgName = organization.getName();
      JsonObject newProject = new JsonObject();
      newProject.addProperty(Credentials.NAME, orgName);
      return TodoistClient.forProject(connection, newProject).addProject(orgName);
   }

   @Override
   public List<Credential> createEnvironment(EnvironmentContext environmentContext, Environment environment) {
      Connection connection = environmentContext.getConnection();
      Organization organization = environmentContext.getOrganization();
      String parentProjectId = organization.getAccount().getCredential(Credentials.ID);
      String environmentName = environment.getName();

      JsonObject newProject = new JsonObject();
      newProject.addProperty(Credentials.NAME, environmentName);
      newProject.addProperty(Credentials.PARENT_ID, parentProjectId);
      return TodoistClient.forProject(connection, newProject).addProject(environmentName);
   }

   @Override
   public List<Credential> createUser(UserContext userContext, User user) {
      Connection connection = userContext.getConnection();
      return TodoistClient.forUser(connection, user).addUser();
   }

   @Override
   public void updateEnvironment(EnvironmentContext environmentContext, Environment updatedEnvironment) { }

   @Override
   public List<Credential> addUserToEnvironment(EnvironmentContext environmentContext, User user) {
      return updateProjectWithUserMembership(environmentContext, user, TodoistAction.SHARE_PROJECT);
   }

   @Override
   public void removeUserFromEnvironment(EnvironmentContext environmentContext, User user) {
      updateProjectWithUserMembership(environmentContext, user, TodoistAction.DELETE_COLLABORATOR);
   }

   @Override
   public List<EnvironmentServiceDetail> getEnvironmentServiceDetails(EnvironmentContext environmentContext) {
      return null;
   }

   @Override
   public void deleteUser(UserContext userContext) {
      Connection connection = userContext.getConnection();
      Account userAccount = userContext.getUserAccount();
      User user = userContext.getUser();
      String usersTodoistToken = userAccount.getCredential(Credentials.TOKEN);
      String userSecret = userAccount.getCredential(Credentials.PASSWORD);
      TodoistClient.forUser(connection, user).deleteUser(usersTodoistToken, userSecret);
   }

   @Override
   public void deleteOrganization(OrganizationContext organizationContext) {
      Connection connection = organizationContext.getConnection();
      Organization organization = organizationContext.getOrganization();
      Account orgAccount = organization.getAccount();
      String orgName = organization.getName();
      deleteTodoistProject(connection, orgAccount, orgName);
   }

   @Override
   public void deleteEnvironment(EnvironmentContext environmentContext) {
      Connection connection = environmentContext.getConnection();
      Environment environment = environmentContext.getEnvironment();
      Account environmentAccount = environment.getAccount();
      String envName = environment.getName();
      deleteTodoistProject(connection, environmentAccount, envName);
   }

   private void deleteTodoistProject(Connection connection, Account account, String projectName) {
      String projectId = account.getCredential(Credentials.ID);
      JsonObject projectToDelete = new JsonObject();
      projectToDelete.addProperty(Credentials.ID, Long.parseLong(projectId));
      TodoistClient.forProject(connection, projectToDelete).deleteProject(projectName);
   }

   private List<Credential> updateProjectWithUserMembership(EnvironmentContext environmentContext, User user, TodoistAction action) {
      Connection connection = environmentContext.getConnection();
      Environment environment = environmentContext.getEnvironment();
      String parentProjectId = environment.getAccount().getCredential(Credentials.ID);
      String environmentName = environment.getName();
      String userEmail = user.getAccount().getCredential(Credentials.EMAIL);

      JsonObject updatedProject = new JsonObject();
      updatedProject.addProperty(Credentials.PROJECT_ID, parentProjectId);
      updatedProject.addProperty(Credentials.EMAIL, userEmail);
      TodoistClient client = TodoistClient.forProject(connection, updatedProject);
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
