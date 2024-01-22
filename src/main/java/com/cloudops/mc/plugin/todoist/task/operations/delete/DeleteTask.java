package com.cloudops.mc.plugin.todoist.task.operations.delete;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.cloudops.mc.plugin.sdk.annotations.EntityOperation;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.models.Connection;
import com.cloudops.mc.plugin.sdk.operation.Operation;
import com.cloudops.mc.plugin.sdk.operation.OperationError;
import com.cloudops.mc.plugin.sdk.operation.OperationResult;
import com.cloudops.mc.plugin.todoist.Credentials;
import com.cloudops.mc.plugin.todoist.lib.client.TodoistTasksClient;
import com.cloudops.mc.plugin.todoist.task.Task;
import com.google.gson.JsonObject;

@EntityOperation(value = "delete")  // <--------- Operation type with annotation
public class DeleteTask implements Operation<Task> {

   @Override
   public List<OperationError> precondition(CallerContext callerContext, String id) {
      // No preconditions added. Try adding some precondition checks.
      // (e.g: Cannot delete if there are only 3 tasks left)
      return Collections.emptyList();
   }

   @Override
   public List<OperationError> validate(CallerContext callerContext, Task task) {
      // No validations added. Try adding some validation checks.
      // (e.g: Cannot delete if the due date is less than 24 hours away)
      return Collections.emptyList();
   }

   @Override
   public OperationResult execute(CallerContext callerContext, Task task) {
      JsonObject taskJson = new JsonObject();
      // The Todoist delete API requires just the `id` of the task to be deleted
      taskJson.addProperty(Credentials.ID, task.getId());
      Connection connection = callerContext.getConnection();
      TodoistTasksClient client = TodoistTasksClient.getFor(connection, taskJson);
      Optional<Task> taskDeleteResult = client.delete();
      // respond with the deleted Task information
      return new OperationResult.Builder(callerContext).withEntity(taskDeleteResult.orElse(null)).build();
   }
}

