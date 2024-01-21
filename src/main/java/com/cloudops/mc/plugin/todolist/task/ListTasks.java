package com.cloudops.mc.plugin.todolist.task;

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

@ViewBuilder                                              // <------------- (1)
public class ListTasks extends ListViewBuilder<Task> {    // <------------- (2)
   public ListTasks(EntityFetcher<Task> fetcher) {         // <------------- (3)
      super(fetcher);
   }

   /*
    * Returns a list of `FieldMetadata` objects that contains all the fields of the Entity object
    * that must be shown in the list view of the entity. The metadata also includes the labels to
    * use as column headers for each of these fields
    */
   @Override
   protected List<FieldMetadata> getFields(CallerContext callerContext) {
      List<FieldMetadata> taskListColumns = new ArrayList<>();
      taskListColumns.add(new FieldValue.Builder("name", "todolist.fields.name").build());
      taskListColumns.add(new FieldValue.Builder("due", "todolist.fields.due").isDate().build());
      taskListColumns.add(new FieldValue.Builder("dateString", "todolist.fields.dateString").build());
      taskListColumns.add(new FieldValue.Builder("isRecurring", "todolist.fields.recurring").build());
      taskListColumns.add(new FieldValue.Builder("addedAt", "todolist.fields.added_at").isDate().build());
      return taskListColumns;
   }

   /*
    * Returns a list of general operations available on the entity that will be shown in the entity
    * list page. These are operations not specific to one single entity instance.
    *     - Example: 'create' operation
    */
   @Override
   protected List<OperationMetadata> getGeneralOperations() {
      // To be completed in the `Entity Operation` section of the guide
      return Collections.singletonList(
              // Use the same identifier used for the operation name - 'create' and provide an ICON
              Metadata.operation("create", Icon.PLUS).build());
   }

   /*
    * Returns a list of specific operations available on each entity that will be shown in the entity
    * list page. These are operation specific to one instance and are triggered per instance of the entity.
    *     - Example: 'delete' operation
    */
   @Override
   protected List<OperationMetadata> getSpecificOperations() {
      // To be completed in the `Entity Operation` section of the guide

      return Collections.singletonList(Metadata.operation("delete", Icon.MINUS).build());
   }
}

