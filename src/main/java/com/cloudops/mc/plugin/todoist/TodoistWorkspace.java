package com.cloudops.mc.plugin.todoist;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.ConnectionContext;
import com.cloudops.mc.plugin.sdk.viewbuilders.WorkspaceViewBuilder;
import com.cloudops.mc.plugin.sdk.viewbuilders.response.WorkspaceView;

@ViewBuilder
public class TodoistWorkspace implements WorkspaceViewBuilder {
   @Override
   public WorkspaceView buildView(ConnectionContext connectionContext) {
      return WorkspaceView.create()
              .tab("todoist.service.tasks.name", "instances")
              .build();
   }
}
