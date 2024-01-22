package com.cloudops.mc.plugin.todoist.task.operations.create;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.ViewBuilder;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.operation.OperationSection;
import com.cloudops.mc.plugin.sdk.operation.OperationStep;
import com.cloudops.mc.plugin.sdk.operation.StepResult;
import com.cloudops.mc.plugin.sdk.ui.form.Form;
import com.cloudops.mc.plugin.sdk.ui.form.FormElement;
import com.cloudops.mc.plugin.sdk.viewbuilders.OperationViewBuilder;
import com.cloudops.mc.plugin.todoist.task.Task;

@ViewBuilder(name="create")
public class CreateTaskView extends OperationViewBuilder<Task> {
   @Override
   protected List<OperationSection<Task>> getSections() {
      return singletonList(section("TaskInfo", this::details));
   }

   // supplementary method that builds a specific step for Task details
   private StepResult<Task> details(CallerContext callerContext, Task task) {
      // a list of Form elements in the details section
      List<FormElement> elements = new ArrayList<>();
      // Adding a `text input` form element
      elements.add(Form.text("name")
              .label("todolist.fields.name")
              .required()
              .build());

      elements.add(Form.text("due")
              .label("todolist.fields.due")
              .description("todolist.fields.due_description")
              .required()
              .build());

      elements.add(Form.text("dateString")
              .label("todolist.fields.dateString")
              .description("todolist.fields.dateString_description")
              .build());

      // Adding a `checkbox` form element
      elements.add(Form.checkbox("isRecurring")
              .label("todolist.fields.recurring")
              .build());
      return new StepResult<>(elements, task);
   }

   @Override
   protected List<OperationStep<Task>> getSteps() {
      OperationStep taskInfo = stepBuilder("Task Info", "Input the task to be completed.",
              singletonList(section("TaskName", this::toDoItem))).build();
      OperationStep dueDate = stepBuilder("Due Date", "Input the date your task should be completed by.",
              singletonList(section("TaskDueDate", this::dueDate))).build();
      OperationStep isRecurringTask = stepBuilder("Recurring Task",
              "Check the box if the task will be recurring.",
              singletonList(section("RecurringTask", this::recurringTask))).build();

      return Arrays.asList(taskInfo, dueDate, isRecurringTask);
   }

   // supplementary method that builds a specific step for Task details
   private StepResult<Task> toDoItem(CallerContext callerContext, Task task) {
      // a list of Form elements in the details section
      List<FormElement> elements = new ArrayList<>();
      // Adding a `text input` form element
      elements.add(Form.text("name")
              .label("todolist.fields.name")
              .required()
              .build());

      return new StepResult<>(elements, task);
   }

   private StepResult<Task> dueDate(CallerContext callerContext, Task task) {
      List<FormElement> elements = new ArrayList<>();

      elements.add(Form.text("due")
              .label("todolist.fields.due")
              .description("todolist.fields.due_description")
              .required()
              .build());

      elements.add(Form.text("dateString")
              .label("todolist.fields.dateString")
              .description("todolist.fields.dateString_description")
              .build());

      return new StepResult<>(elements, task);
   }

   private StepResult<Task> recurringTask(CallerContext callerContext, Task task) {
      List<FormElement> elements = new ArrayList<>();

      elements.add(Form.checkbox("isRecurring")
              .label("todolist.fields.recurring")
              .build());
      return new StepResult<>(elements, task);
   }
}
