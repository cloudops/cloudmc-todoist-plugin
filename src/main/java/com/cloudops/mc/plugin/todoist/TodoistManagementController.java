package com.cloudops.mc.plugin.todoist;

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
import com.cloudops.mc.plugin.sdk.models.Credential;
import com.cloudops.mc.plugin.sdk.models.Environment;
import com.cloudops.mc.plugin.sdk.models.Organization;
import com.cloudops.mc.plugin.sdk.models.User;

/**
 * The TodoistManagementController handles all org, env and user management on the service
 */
@PluginComponent
public class TodoistManagementController implements OrganizationController, UserController, EnvironmentController {
   private static final Logger logger = LoggerFactory.getLogger(TodoistManagementController.class);

   @Override
   public List<Credential> createOrganization(OrganizationContext organizationContext, Organization organization) {
      return new ArrayList<>();
   }

   @Override
   public void deleteOrganization(OrganizationContext organizationContext) {

   }

   @Override
   public List<Credential> createUser(UserContext userContext, User user) {
      return new ArrayList<>();
   }

   @Override
   public void deleteUser(UserContext userContext) {

   }

   @Override
   public List<Credential> createEnvironment(EnvironmentContext environmentContext, Environment environment) {
      return new ArrayList<>();
   }

   @Override
   public void updateEnvironment(EnvironmentContext environmentContext, Environment updatedEnvironment) {

   }

   @Override
   public void deleteEnvironment(EnvironmentContext environmentContext) {

   }

   @Override
   public List<Credential> addUserToEnvironment(EnvironmentContext environmentContext, User user) {
      return new ArrayList<>();
   }

   @Override
   public void removeUserFromEnvironment(EnvironmentContext environmentContext, User user) {

   }

   @Override
   public List<EnvironmentServiceDetail> getEnvironmentServiceDetails(EnvironmentContext environmentContext) {
      return new ArrayList<>();
   }
}
