package com.cloudops.mc.plugin.todolist.task;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cloudops.mc.plugin.sdk.annotations.Fetcher;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.sdk.fetcher.AbstractFetcher;
import com.cloudops.mc.plugin.sdk.fetcher.FetchOptions;
import com.cloudops.mc.plugin.sdk.fetcher.FetcherError;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Environment;
import com.cloudops.mc.plugin.todolist.Credentials;
import com.cloudops.mc.plugin.todolist.lib.client.TodoistFetchClient;
import com.cloudops.mc.plugin.todolist.lib.utils.TodoistResourceType;

@Component                                                  // <------------- (1)
@Fetcher                                                    // <------------- (2)
public class TaskFetcher extends AbstractFetcher<Task> {    // <------------- (3)

   /*
    * Fetch the list of `Task`(s) scoped to context information set in the given `CallerContext`.
    * The 'FetchOptions' can be used to pass filter properties to the fetcher.
    */
   @Override
   protected List<Task> fetchEntities(CallerContext callerContext, FetchOptions fetchOptions) {
      List<Task> tasksList = Collections.emptyList();
      // The callerContext contains the information as to which Todoist connection is to be reached
      Connection connection = callerContext.getConnection();
      // Get the CMC-Environment within which this fetch was triggered
      Environment environment = callerContext.getEnvironment();
      // The Environment account holds the credentials of the environment as returned in the
      // createEnvironment() method of the EnvironmentController.
      String envId = environment.getAccount().getCredential(Credentials.ID);
      try {
         // Use the TodoistClient in the lib package to fetch the Todoist entities
         TodoistFetchClient todoistClient = TodoistFetchClient.getFor(connection);
         List<? extends ServiceEntity> entities = todoistClient.fetch(TodoistResourceType.TASKS);
         tasksList = entities.stream()
                 .map(Task.class::cast)
                 .filter(task -> task.getProjectId().equals(envId))
                 .collect(Collectors.toList());
      } catch (IOException e) {
         e.printStackTrace();
      }
      return tasksList;
   }

   /*
    * Fetch the specific `Task` scoped to the context information set in the given `CallerContext` and the given Id.
    * The 'FetchOptions' can be used to pass filter properties to the fetcher.
    */
   @Override
   protected Task fetchEntity(CallerContext callerContext, String id, FetchOptions fetchOptions) {
      List<Task> tasksList = fetchEntities(callerContext, fetchOptions);
      return tasksList.stream()
              .filter(task -> task.getId().equals(id))
              .findFirst()
              .orElse(null);
   }

   @Override
   protected List<FetcherError> validateListFetchOptions(CallerContext callerContext, FetchOptions fetchOptions) {
      return Collections.emptyList();
   }

   @Override
   protected List<FetcherError> validateDetailFetchOptions(CallerContext callerContext, FetchOptions fetchOptions) {
      return Collections.emptyList();
   }
}