package com.cloudops.mc.plugin.todoist.lib.utils;

/**
 * Update the package name with the correct package
 * package <packageName>.<pluginShortName>.lib.utils;
 */

import com.cloudops.mc.plugin.sdk.error.ErrorCode;

public enum TodoistErrors implements ErrorCode {
   CONNECTION_TEST_ERROR("There was an error when trying to connect to the todoist API"),
   CONNECTION_TEST_FAILED("The todoist API returned an failure response"),

   MAXIMUM_ALLOWED_TASKS_REACHED("Maximum allowed tasks per environment reached"),

   INVALID_DATE_FORMAT("Invalid format for the date. Must be in the form: [YYYY-MM-DD]"),
   INVALID_YEAR_IN_DUE_DATE("Invalid year in the due date. Must be in the form: [YYYY]"),
   INVALID_MONTH_IN_DUE_DATE("Invalid month in the due date. Must be in the form: [MM] and between 01-12"),
   INVALID_DAY_IN_DUE_DATE("Invalid day in the due date. Must be in the form: [DD] and between 01-31"),

   INVALID_TIME_FORMAT("Invalid format for the time. Must be in the form: [HH:MM]"),
   INVALID_HOUR_IN_DUE_DATE("Invalid hour in the due date. Must be in the form: [HH] and between 00-23"),
   INVALID_MINUTE_IN_DUE_DATE("Invalid minute in the due date. Must be in the form: [MM] and between 00-59"),

   INVALID_DUE_DATE_FORMAT("Invalid task due date. Must be in the form: [YYYY-MM-DD HH:MM]");

   private String message;

   TodoistErrors(String message) {
      this.message = message;
   }

   @Override
   public String message() {
      return this.message;
   }
}
