package com.cloudops.mc.plugin.todoist.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cloudops.mc.plugin.sdk.annotations.Fetcher;
import com.cloudops.mc.plugin.sdk.contexts.CallerContext;
import com.cloudops.mc.plugin.sdk.fetcher.AbstractFetcher;
import com.cloudops.mc.plugin.sdk.fetcher.FetchOptions;
import com.cloudops.mc.plugin.sdk.fetcher.FetcherError;

@Fetcher
public class InstanceFetcher extends AbstractFetcher<Instance> {

   @Override
   protected List<Instance> fetchEntities(CallerContext callerContext, FetchOptions fetchOptions) {
      return new ArrayList<>();
   }

   @Override
   protected Instance fetchEntity(CallerContext callerContext, String id, FetchOptions fetchOptions) {
      return new Instance();
   }

   @Override
   protected List<FetcherError> validateListFetchOptions(CallerContext callerContext, FetchOptions fetchOptions) {
      return Collections.emptyList();
   }

   @Override
   protected List<FetcherError> validateDetailFetchOptions(CallerContext callerContext, FetchOptions fetchOptions) {
      return Collections.emptyList();
   }
}
