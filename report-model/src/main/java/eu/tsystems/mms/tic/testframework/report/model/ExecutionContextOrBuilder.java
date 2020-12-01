// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: framework.proto

package eu.tsystems.mms.tic.testframework.report.model;

public interface ExecutionContextOrBuilder extends
    // @@protoc_insertion_point(interface_extends:data.ExecutionContext)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.data.ContextValues context_values = 1;</code>
   * @return Whether the contextValues field is set.
   */
  boolean hasContextValues();
  /**
   * <code>.data.ContextValues context_values = 1;</code>
   * @return The contextValues.
   */
  eu.tsystems.mms.tic.testframework.report.model.ContextValues getContextValues();
  /**
   * <code>.data.ContextValues context_values = 1;</code>
   */
  eu.tsystems.mms.tic.testframework.report.model.ContextValuesOrBuilder getContextValuesOrBuilder();

  /**
   * <pre>
   *    repeated string merged_class_context_ids = 3 [deprecated = true];
   *    repeated ContextClip exit_points = 4 [deprecated = true];
   *    repeated ContextClip failure_ascpects = 5 [deprecated = true];
   *repeated string suite_context_ids = 6 [deprecated = true];
   * </pre>
   *
   * <code>.data.RunConfig run_config = 7;</code>
   * @return Whether the runConfig field is set.
   */
  boolean hasRunConfig();
  /**
   * <pre>
   *    repeated string merged_class_context_ids = 3 [deprecated = true];
   *    repeated ContextClip exit_points = 4 [deprecated = true];
   *    repeated ContextClip failure_ascpects = 5 [deprecated = true];
   *repeated string suite_context_ids = 6 [deprecated = true];
   * </pre>
   *
   * <code>.data.RunConfig run_config = 7;</code>
   * @return The runConfig.
   */
  eu.tsystems.mms.tic.testframework.report.model.RunConfig getRunConfig();
  /**
   * <pre>
   *    repeated string merged_class_context_ids = 3 [deprecated = true];
   *    repeated ContextClip exit_points = 4 [deprecated = true];
   *    repeated ContextClip failure_ascpects = 5 [deprecated = true];
   *repeated string suite_context_ids = 6 [deprecated = true];
   * </pre>
   *
   * <code>.data.RunConfig run_config = 7;</code>
   */
  eu.tsystems.mms.tic.testframework.report.model.RunConfigOrBuilder getRunConfigOrBuilder();

  /**
   * <code>string project_Id = 8;</code>
   * @return The projectId.
   */
  java.lang.String getProjectId();
  /**
   * <code>string project_Id = 8;</code>
   * @return The bytes for projectId.
   */
  com.google.protobuf.ByteString
      getProjectIdBytes();

  /**
   * <code>string job_Id = 9;</code>
   * @return The jobId.
   */
  java.lang.String getJobId();
  /**
   * <code>string job_Id = 9;</code>
   * @return The bytes for jobId.
   */
  com.google.protobuf.ByteString
      getJobIdBytes();

  /**
   * <code>string run_Id = 10;</code>
   * @return The runId.
   */
  java.lang.String getRunId();
  /**
   * <code>string run_Id = 10;</code>
   * @return The bytes for runId.
   */
  com.google.protobuf.ByteString
      getRunIdBytes();

  /**
   * <code>string task_Id = 11;</code>
   * @return The taskId.
   */
  java.lang.String getTaskId();
  /**
   * <code>string task_Id = 11;</code>
   * @return The bytes for taskId.
   */
  com.google.protobuf.ByteString
      getTaskIdBytes();

  /**
   * <code>repeated string exclusive_session_context_ids = 12;</code>
   * @return A list containing the exclusiveSessionContextIds.
   */
  java.util.List<java.lang.String>
      getExclusiveSessionContextIdsList();
  /**
   * <code>repeated string exclusive_session_context_ids = 12;</code>
   * @return The count of exclusiveSessionContextIds.
   */
  int getExclusiveSessionContextIdsCount();
  /**
   * <code>repeated string exclusive_session_context_ids = 12;</code>
   * @param index The index of the element to return.
   * @return The exclusiveSessionContextIds at the given index.
   */
  java.lang.String getExclusiveSessionContextIds(int index);
  /**
   * <code>repeated string exclusive_session_context_ids = 12;</code>
   * @param index The index of the value to return.
   * @return The bytes of the exclusiveSessionContextIds at the given index.
   */
  com.google.protobuf.ByteString
      getExclusiveSessionContextIdsBytes(int index);

  /**
   * <code>repeated .data.PLogMessage log_messages = 14;</code>
   */
  java.util.List<eu.tsystems.mms.tic.testframework.report.model.PLogMessage> 
      getLogMessagesList();
  /**
   * <code>repeated .data.PLogMessage log_messages = 14;</code>
   */
  eu.tsystems.mms.tic.testframework.report.model.PLogMessage getLogMessages(int index);
  /**
   * <code>repeated .data.PLogMessage log_messages = 14;</code>
   */
  int getLogMessagesCount();
  /**
   * <code>repeated .data.PLogMessage log_messages = 14;</code>
   */
  java.util.List<? extends eu.tsystems.mms.tic.testframework.report.model.PLogMessageOrBuilder> 
      getLogMessagesOrBuilderList();
  /**
   * <code>repeated .data.PLogMessage log_messages = 14;</code>
   */
  eu.tsystems.mms.tic.testframework.report.model.PLogMessageOrBuilder getLogMessagesOrBuilder(
      int index);

  /**
   * <code>int32 estimated_tests_count = 15;</code>
   * @return The estimatedTestsCount.
   */
  int getEstimatedTestsCount();
}
