package com.cloudops.mc.plugin.todolist.task.operations.delete;

import com.cloudops.mc.plugin.sdk.annotations.ConfirmDialog;
import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.ui.form.Form;
import com.cloudops.mc.plugin.sdk.ui.form.MessageElement;
import com.cloudops.mc.plugin.sdk.viewbuilders.ConfirmationOperationViewBuilder;
import com.cloudops.mc.plugin.todolist.task.Task;

@ConfirmDialog
@ViewBuilder(name = "delete")
public class DeleteTaskView extends ConfirmationOperationViewBuilder<Task> {

   @Override
   protected MessageElement getMessage(Task task) {
      // labels added here must be updated in the label.json file
      return Form.message("todolist.service.tasks.operations.delete.confirm")
              .interpolate("name", task.getName())
              .build();
   }
}

