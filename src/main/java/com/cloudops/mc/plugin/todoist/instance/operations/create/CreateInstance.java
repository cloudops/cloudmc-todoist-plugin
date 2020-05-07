package com.cloudops.mc.plugin.todoist.instance.operations.create;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudops.mc.plugin.sdk.annotations.EntityOperation;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.operation.Operation;
import com.cloudops.mc.plugin.sdk.operation.OperationError;
import com.cloudops.mc.plugin.sdk.operation.OperationResult;
import com.cloudops.mc.plugin.todoist.instance.Instance;

@EntityOperation(value = "create", defaultForType = EntityOperation.Type.CREATE)
public class CreateInstance implements Operation<Instance> {
   private static final Logger logger = LoggerFactory.getLogger(CreateInstance.class);

   @Override
   public List<OperationError> precondition(CallerContext callerContext, String instanceId) {
      return Collections.emptyList();
   }

   @Override
   public List<OperationError> validate(CallerContext callerContext, Instance instance) {
      return Collections.emptyList();
   }

   @Override
   public OperationResult execute(CallerContext callerContext, Instance instance) {
      return new OperationResult.Builder(callerContext).withEntity(instance).build();
   }
}
