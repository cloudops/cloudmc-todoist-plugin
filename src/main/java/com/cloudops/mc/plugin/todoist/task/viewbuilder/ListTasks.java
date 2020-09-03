package com.cloudops.mc.plugin.todoist.task.viewbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.fetcher.EntityFetcher;
import com.cloudops.mc.plugin.sdk.ui.Icon;
import com.cloudops.mc.plugin.sdk.ui.metadata.FieldMetadata;
import com.cloudops.mc.plugin.sdk.ui.metadata.FieldValue;
import com.cloudops.mc.plugin.sdk.ui.metadata.Metadata;
import com.cloudops.mc.plugin.sdk.ui.metadata.OperationMetadata;
import com.cloudops.mc.plugin.sdk.viewbuilders.ListViewBuilder;
import com.cloudops.mc.plugin.todoist.task.Task;

@ViewBuilder
public class ListTasks extends ListViewBuilder<Task> {

   public ListTasks(EntityFetcher<Task> fetcher) {
      super(fetcher);
   }

   @Override
   protected List<FieldMetadata> getFields(CallerContext callerContext) {
      List<FieldMetadata> taskListColumns = new ArrayList<>();
      taskListColumns.add(new FieldValue.Builder("name", "todoist.fields.name").build());
      taskListColumns.add(new FieldValue.Builder("dueDate.date", "todoist.fields.due").build());
      taskListColumns.add(new FieldValue.Builder("dueDate.dateString", "todoist.fields.dueString").build());
      taskListColumns.add(new FieldValue.Builder("dueDate.isRecurring", "todoist.fields.recurring").build());
      return taskListColumns;
   }

   @Override
   protected List<OperationMetadata> getGeneralOperations() {
      return Collections.singletonList(Metadata.operation("create", Icon.PLUS).build());
   }

   @Override
   protected List<OperationMetadata> getSpecificOperations() {
      return Collections.emptyList();
   }
}
