package com.cloudops.mc.plugin.todoist.task;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cloudops.mc.plugin.sdk.annotations.Fetcher;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.entity.ServiceEntity;
import com.cloudops.mc.plugin.sdk.fetcher.AbstractFetcher;
import com.cloudops.mc.plugin.sdk.fetcher.FetchOptions;
import com.cloudops.mc.plugin.sdk.fetcher.FetcherError;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.models.Environment;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.TodoistClient;
import com.cloudops.mc.plugin.todoist.utils.TodoistResources;

@Fetcher
public class TaskFetcher extends AbstractFetcher<Task> {

   @Override
   protected List<Task> fetchEntities(CallerContext callerContext, FetchOptions fetchOptions) {
      List<Task> tasksList = Collections.emptyList();
      Connection connection = callerContext.getConnection();
      Environment environment = callerContext.getEnvironment();
      String envId = environment.getAccount().getCredential(Credentials.ID);
      try {
         TodoistClient todoistClient = TodoistClient.forResources(connection);
         List<? extends ServiceEntity> entities = todoistClient.fetch(TodoistResources.TASKS);
         tasksList = entities.stream()
                 .map(Task.class::cast)
                 .filter(task -> task.getProjectId().equals(envId))
                 .collect(Collectors.toList());
      } catch (IOException e) {
         e.printStackTrace();
      }
      return tasksList;
   }

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
