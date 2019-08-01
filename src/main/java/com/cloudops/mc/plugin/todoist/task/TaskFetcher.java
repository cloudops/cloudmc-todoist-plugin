package com.cloudops.mc.plugin.todoist.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.Fetcher;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.fetcher.AbstractFetcher;
import com.cloudops.mc.plugin.sdk.fetcher.FetchOptions;
import com.cloudops.mc.plugin.sdk.fetcher.FetcherError;

@Fetcher
public class TaskFetcher extends AbstractFetcher<Task> {

   @Override
   protected List<Task> fetchEntities(CallerContext callerContext, FetchOptions fetchOptions) {
      List<Task> tasksList = new ArrayList<>();
//      Connection connection = callerContext.getConnection();
//      Environment environment = callerContext.getEnvironment();
//      String envId = environment.getAccount().getCredential(Credentials.ID);
//      try {
//         String todoistUrl = TodoistClientUtil.getFormattedURL(connection.getParameter(Credentials.URL) + Credentials.SYNC);
//         String todoistToken = connection.getParameter(Credentials.TOKEN);
//
//         MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
//         multipartBuilder.addTextBody(Credentials.TOKEN, todoistToken);
//         multipartBuilder.addTextBody(Credentials.RESOURCE_TYPES, "[\"" + Credentials.TASKS_RESOURCE + "\"]");
//         HttpPost postRequest = TodoistClientUtil.getPostRequest(multipartBuilder, todoistUrl);
//
//         CloseableHttpClient httpClient = HttpClients.createDefault();
//         CloseableHttpResponse response = httpClient.execute(postRequest);
//         JsonArray tasksArray = TodoistClientUtil.doRedirectForFetch(response, todoistToken, Credentials.TASKS_RESOURCE);
//         for (JsonElement taskItem: tasksArray) {
//            JsonObject taskObject = taskItem.getAsJsonObject();
//            String projectId = taskObject.get(Credentials.PROJECT_ID).getAsString();
//            if (!envId.equals(projectId)) {
//               continue;
//            }
//
//            String id = taskObject.get(Credentials.ID).getAsString();
//            String name = taskObject.get(Credentials.CONTENT).getAsString();
//            Task newTask = new Task();
//            newTask.setId(id);
//            newTask.setName(name);
//            newTask.setProjectId(projectId);
//
//            JsonObject due = taskObject.getAsJsonObject(Credentials.DUE_DATE);
//            if (due != null) {
//               String dateOnDue = due.get(Credentials.DATE).getAsString();
//               String dateString = due.get(Credentials.DATE_STRING).getAsString();
//               boolean isRecurring = due.get(Credentials.DATE_IS_RECURRING).getAsBoolean();
//               Date dateObject = newTask.fromDateString(dateOnDue);
//               Task.DateString datePhrase = newTask.fromDatePhrase(dateString);
//               DueDate dueDate = newTask.getDueDate();
//               dueDate.setDate(dateObject);
//               dueDate.setDateString(datePhrase);
//               dueDate.setRecurring(isRecurring);
//               newTask.setDueDate(dueDate);
//            }
//            tasksList.add(newTask);
//         }
//      } catch (IOException e) {
//         e.printStackTrace();
//      }
      return tasksList;
   }

   @Override
   protected Task fetchEntity(CallerContext callerContext, String id, FetchOptions fetchOptions) {
      List<Task> tasksList = fetchEntities(callerContext, fetchOptions);
      for (Task task: tasksList) {
         if (task.getId().equals(id)) {
            return task;
         }
      }
      return null;
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
