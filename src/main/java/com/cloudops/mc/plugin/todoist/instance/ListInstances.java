package com.cloudops.mc.plugin.todoist.instance;

import java.util.Arrays;
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

@ViewBuilder
public class ListInstances extends ListViewBuilder<Instance> {

   public ListInstances(EntityFetcher<Instance> fetcher) {
      super(fetcher);
   }

   @Override
   protected List<FieldMetadata> getFields(CallerContext callerContext) {
      return Arrays.asList(new FieldValue.Builder("name", "todo.fields.name").build());
   }

   @Override
   protected List<OperationMetadata> getGeneralOperations() {
      return Arrays.asList(Metadata.operation("create", Icon.PLUS).build());
   }

   @Override
   protected List<OperationMetadata> getSpecificOperations() {
      return Collections.emptyList();
   }
}
