//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

package edu.iu.dsc.tws.api.task.executor;

import edu.iu.dsc.tws.api.config.Config;
import edu.iu.dsc.tws.api.task.graph.DataFlowTaskGraph;
import edu.iu.dsc.tws.api.task.schedule.elements.TaskSchedulePlan;

public interface IExecutionPlanBuilder {
  /**
   * Schedule and execution
   * @param taskGraph the task graph
   * @param taskSchedule the task schedule
   * @return the execution created and null if nothing to execute
   */
  ExecutionPlan build(Config cfg, DataFlowTaskGraph taskGraph,
                      TaskSchedulePlan taskSchedule);
}
