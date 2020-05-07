package com.cloudops.mc.plugin.todoist.instance.operations.create;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.operation.OperationSection;
import com.cloudops.mc.plugin.sdk.operation.StepResult;
import com.cloudops.mc.plugin.sdk.ui.form.Form;
import com.cloudops.mc.plugin.sdk.ui.form.FormElement;
import com.cloudops.mc.plugin.sdk.viewbuilders.OperationViewBuilder;
import com.cloudops.mc.plugin.todoist.instance.Instance;

@ViewBuilder(name = "create")
public class CreateInstanceView extends OperationViewBuilder<Instance> {

   @Override
   protected List<OperationSection<Instance>> getSections() {
      return Arrays.asList(section("details", this::details));
   }

   private StepResult<Instance> details(CallerContext callerContext, Instance instance) {
      List<FormElement> elements = new ArrayList<>();
      elements.add(Form.text("name").label("todo.fields.name").required().build());
      return new StepResult<>(elements, instance);
   }
}
