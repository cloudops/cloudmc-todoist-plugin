package com.cloudops.mc.plugin.todoist.task.operations;

import java.util.Collections;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.EntityOperation;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.operation.Operation;
import com.cloudops.mc.plugin.sdk.operation.OperationError;
import com.cloudops.mc.plugin.sdk.operation.OperationResult;
import com.cloudops.mc.plugin.todoist.task.Task;

@EntityOperation(value = "create", defaultForType = EntityOperation.Type.CREATE)
public class CreateTask implements Operation<Task> {

   @Override
   public List<OperationError> precondition(CallerContext callerContext, String entityId) {
      return Collections.emptyList();
   }

   @Override
   public List<OperationError> validate(CallerContext callerContext, Task entity) {
      return Collections.emptyList();
   }

   @Override
   public OperationResult execute(CallerContext callerContext, Task task) {
      return new OperationResult.Builder(callerContext).withEntity(task).build();
   }
}
