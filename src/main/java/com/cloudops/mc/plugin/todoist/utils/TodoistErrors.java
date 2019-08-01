package com.cloudops.mc.plugin.todoist.utils;

import com.cloudops.mc.plugin.sdk.error.ErrorCode;

public enum TodoistErrors implements ErrorCode {
   CONNECTION_TEST_ERROR("There was an error when trying to connect to the todoist API"),
   CONNECTION_TEST_FAILED("The todoist API returned an failure response");

   private String message;

   TodoistErrors(String message) {
      this.message = message;
   }

   @Override
   public String message() {
      return this.message;
   }
}
