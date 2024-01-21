package com.cloudops.mc.plugin.todolist;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.ConnectionContext;
import com.cloudops.mc.plugin.sdk.viewbuilders.WorkspaceViewBuilder;
import com.cloudops.mc.plugin.sdk.viewbuilders.response.WorkspaceView;

@ViewBuilder(isDefault = true)
public class TodolistWorkspace implements WorkspaceViewBuilder {
   @Override
   public WorkspaceView buildView(ConnectionContext connectionContext) {
      return WorkspaceView.create()
              .tab("todolist.service.tasks.name", "tasks")
              .build();
   }
}

