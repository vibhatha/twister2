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
package edu.iu.dsc.tws.tset.cdfw;

import edu.iu.dsc.tws.api.compute.executor.ExecutionPlan;
import edu.iu.dsc.tws.api.compute.graph.ComputeGraph;
import edu.iu.dsc.tws.api.dataset.DataObject;
import edu.iu.dsc.tws.task.cdfw.CDFWEnv;
import edu.iu.dsc.tws.task.cdfw.DafaFlowJobConfig;
import edu.iu.dsc.tws.task.cdfw.DataFlowGraph;
import edu.iu.dsc.tws.tset.env.BatchTSetEnvironment;
import edu.iu.dsc.tws.tset.env.BuildContext;
import edu.iu.dsc.tws.tset.sets.BuildableTSet;

public class BatchTSetCDFWEnvironment extends BatchTSetEnvironment {

  private CDFWEnv cdfwEnv;

  public BatchTSetCDFWEnvironment(CDFWEnv env) {
    super();
    this.cdfwEnv = env;
  }

  @Override
  protected <T> DataObject<T> executeBuildContext(BuildContext buildContext,
                                                  BuildableTSet outputTset, boolean isIterative) {
    DafaFlowJobConfig dafaFlowJobConfig = new DafaFlowJobConfig();
    DataFlowGraph job = DataFlowGraph.newSubGraphJob("hello", buildContext.getComputeGraph()).
        setWorkers(2).addDataFlowJobConfig(dafaFlowJobConfig).setGraphType("non-iterative");
    cdfwEnv.executeDataFlowGraph(job);
    return null;
  }

//  @Override
//  protected void pushInputsToFunctions(ComputeGraph graph, ExecutionPlan executionPlan) {
//    return;
//  }
}
