package com.cloudops.mc.plugin.todoist.task.viewbuilder;

import java.util.ArrayList;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.operation.OperationSection;
import com.cloudops.mc.plugin.sdk.operation.StepResult;
import com.cloudops.mc.plugin.sdk.ui.form.Form;
import com.cloudops.mc.plugin.sdk.ui.form.FormElement;
import com.cloudops.mc.plugin.sdk.viewbuilders.OperationViewBuilder;
import com.cloudops.mc.plugin.todoist.task.Task;

@ViewBuilder(name = "create")
public class CreateTaskView extends OperationViewBuilder<Task> {

   private StepResult<Task> details(CallerContext callerContext, Task task) {
      List<FormElement> elements = new ArrayList<>();
      elements.add(Form.text("name").label(".fields.name").required().build());
      return new StepResult<>(elements, task);
   }

   @Override
   protected List<OperationSection<Task>> getSections() {
      return null;
   }
}
