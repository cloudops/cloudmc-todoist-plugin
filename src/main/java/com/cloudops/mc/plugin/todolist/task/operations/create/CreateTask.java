package com.cloudops.mc.plugin.todolist.task.operations.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudops.mc.plugin.sdk.annotations.EntityOperation;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.fetcher.FetchOptions;
import com.cloudops.mc.plugin.sdk.models.Account;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.operation.Operation;
import com.cloudops.mc.plugin.sdk.operation.OperationError;
import com.cloudops.mc.plugin.sdk.operation.OperationResult;
import com.cloudops.mc.plugin.todolist.Credentials;
import com.cloudops.mc.plugin.todolist.lib.client.TodoistTasksClient;
import com.cloudops.mc.plugin.todolist.lib.utils.TodoistErrors;
import com.cloudops.mc.plugin.todolist.task.Task;
import com.cloudops.mc.plugin.todolist.task.TaskFetcher;
import com.google.gson.JsonObject;

@EntityOperation(value="create",defaultForType = EntityOperation.Type.CREATE)
public class CreateTask implements Operation<Task> {

   @Autowired
   private TaskFetcher taskFetcher;

   private static int MAXIMUM_ALLOWED_TASKS=10;
   private static final String MAX_TASKS_LIMIT_REACHED_LABEL= "todolist.service.tasks.max_limit_reached";

   private static final String HYPHEN = "-";
   private static final String COLON = ":";
   private static final String SPACE = " ";
   // new labels that must be added to the label.json
   private static final String INVALID_DUE_DATE_LABEL = "todoist.service.tasks.invalid_due_date";
   private static final String INVALID_DATE_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_date";
   private static final String INVALID_YEAR_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_date_year";
   private static final String INVALID_MONTH_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_date_month";
   private static final String INVALID_DAY_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_date_day";
   private static final String INVALID_TIME_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_time";
   private static final String INVALID_HOUR_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_time_hour";
   private static final String INVALID_MINUTE_IN_DUE_DATE_LABEL = "todoist.service.tasks.invalid_time_minute";

   @Override
   public List<OperationError> precondition(CallerContext callerContext, String id){
      List<OperationError> errors = new ArrayList<>();
      int noOfTasks = taskFetcher.getEntities(callerContext, FetchOptions.none()).getEntities().size();
      if (noOfTasks == MAXIMUM_ALLOWED_TASKS) {
         errors.add(Operation.error(TodoistErrors.MAXIMUM_ALLOWED_TASKS_REACHED)
                 .label(MAX_TASKS_LIMIT_REACHED_LABEL)
                 .context("limit", MAXIMUM_ALLOWED_TASKS)
                 .build());
      }
      return errors;
   }

   @Override
   public List<OperationError> validate(CallerContext callerContext, Task task){
      List<OperationError> errors = new ArrayList<>();
      String due = task.getDue().trim();
      String[] dueParts = due.split(SPACE);
      if (dueParts.length != 2) {
         errors.add(Operation.error(TodoistErrors.INVALID_DUE_DATE_FORMAT)
                 .label(INVALID_DUE_DATE_LABEL)
                 .context("dueDate", due)
                 .build());
      } else {
         checkDate(dueParts[0]).ifPresent(errors::add);
         checkTime(dueParts[1]).ifPresent(errors::add);
      }
      return errors;
   }

   private Optional<OperationError> checkDate(String date) {
      Optional<OperationError> dateError = Optional.empty();
      String[] dateParts = date.split(HYPHEN);
      if (dateParts.length != 3) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_DATE_FORMAT)
                 .label(INVALID_DATE_IN_DUE_DATE_LABEL)
                 .context("date", date)
                 .build());
      }
      String year = dateParts[0];
      if (year.length() != 4 || !StringUtils.isNumeric(year)) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_YEAR_IN_DUE_DATE)
                 .label(INVALID_YEAR_IN_DUE_DATE_LABEL)
                 .context("year", year)
                 .build());
      }
      String month = dateParts[1];
      if (month.length() != 2 || !StringUtils.isNumeric(month) || Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_MONTH_IN_DUE_DATE)
                 .label(INVALID_MONTH_IN_DUE_DATE_LABEL)
                 .context("month", month)
                 .build());
      }
      String day = dateParts[2];
      if (day.length() != 2 || !StringUtils.isNumeric(day) || Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_DAY_IN_DUE_DATE)
                 .label(INVALID_DAY_IN_DUE_DATE_LABEL)
                 .context("day", day)
                 .build());
      }
      return dateError;
   }

   private Optional<OperationError> checkTime(String time) {
      Optional<OperationError> timeError = Optional.empty();
      String[] timeParts = time.split(COLON);
      if (timeParts.length != 2) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_TIME_FORMAT)
                 .label(INVALID_TIME_IN_DUE_DATE_LABEL)
                 .context("time", time)
                 .build());
      }
      String hour = timeParts[0];
      if (hour.length() != 2 || !StringUtils.isNumeric(hour) || Integer.parseInt(hour) > 23 || Integer.parseInt(hour) < 0) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_HOUR_IN_DUE_DATE)
                 .label(INVALID_HOUR_IN_DUE_DATE_LABEL)
                 .context("hour", hour)
                 .build());
      }
      String minute = timeParts[1];
      if (minute.length() != 2 || !StringUtils.isNumeric(minute) || Integer.parseInt(minute) > 59 || Integer.parseInt(minute) < 0) {
         return Optional.of(Operation.error(TodoistErrors.INVALID_MINUTE_IN_DUE_DATE)
                 .label(INVALID_MINUTE_IN_DUE_DATE_LABEL)
                 .context("minute", minute)
                 .build());
      }
      return timeError;
   }

   @Override
   public OperationResult execute(CallerContext callerContext, Task task ){
      JsonObject taskJson = task.toJsonObject();
      Account environmentAccount = callerContext.getEnvironmentAccount();
      // Get the projectId for the new Task from the current environmentAccount
      String projectId = environmentAccount.getCredential(Credentials.ID);
      // update the request json to include the projectId
      taskJson.addProperty(Credentials.PROJECT_ID, projectId);
      Connection connection = callerContext.getConnection();
      // Create a TodoistTasksClient with the connection credentials and the request json
      TodoistTasksClient client = TodoistTasksClient.getFor(connection, taskJson);
      // Invoke the createTask API
      Optional<Task> taskAddResult = client.add();
      // respond with the added new Task object
      return new OperationResult.Builder(callerContext).withEntity(taskAddResult.orElse(null)).build();
   }
}
