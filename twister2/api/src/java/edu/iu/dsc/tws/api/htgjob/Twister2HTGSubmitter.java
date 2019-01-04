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
package edu.iu.dsc.tws.api.htgjob;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import edu.iu.dsc.tws.api.JobConfig;
import edu.iu.dsc.tws.api.Twister2Submitter;
import edu.iu.dsc.tws.api.job.Twister2Job;
import edu.iu.dsc.tws.common.config.Config;
import edu.iu.dsc.tws.proto.system.job.HTGJobAPI;

public final class Twister2HTGSubmitter {

  private static final Logger LOG = Logger.getLogger(Twister2HTGSubmitter.class.getName());

  private Config config;
  private List<HTGJobAPI.ExecuteMessage> executeMessageList = new LinkedList<>();

  public Twister2HTGSubmitter(Config cfg) {
    this.config = cfg;
  }

  /**
   * The executeHTG method first call the schedule method to get the schedule list of the HTG.
   * Then, it invokes the build HTG Job object to build the htg job object for the scheduled graphs.
   */
  public void executeHTG(Twister2Metagraph twister2Metagraph,
                         JobConfig jobConfig,
                         String workerclassName) {

    LOG.fine("HTG Sub Graph Requirements:" + twister2Metagraph.getSubGraph()
        + "\nHTG Relationship Values:" + twister2Metagraph.getRelation());

    List<String> scheduleGraphs = new Twister2HTGScheduler().schedule(twister2Metagraph);
    buildHTGJob(scheduleGraphs, twister2Metagraph, workerclassName, jobConfig);
  }

  /**
   * This method is responsible for building the htg job object which is based on the outcome of
   * the scheduled graphs list.
   */
  private void buildHTGJob(List<String> scheduleGraphs, Twister2Metagraph twister2Metagraph,
                           String workerclassName, JobConfig jobConfig) {

    Twister2Job twister2Job;
    Twister2Metagraph.SubGraph subGraph;
    HTGJobAPI.ExecuteMessage executeMessage;

    //Construct the HTGJob object to be sent to the HTG Driver
    HTGJobAPI.HTGJob htgJob = HTGJobAPI.HTGJob.newBuilder()
        .setHtgJobname(twister2Metagraph.getHTGName())
        .addAllGraphs(twister2Metagraph.getSubGraph())
        .addAllRelations(twister2Metagraph.getRelation())
        .build();

    String subGraphName = scheduleGraphs.get(0);
    subGraph = twister2Metagraph.getMetaGraphMap(subGraphName);

    //Set the subgraph to be executed from the metagraph
    for (String subgraphName : scheduleGraphs) {
      executeMessage = HTGJobAPI.ExecuteMessage.newBuilder()
          .setSubgraphName(subgraphName)
          .build();
      executeMessageList.add(executeMessage);
    }

    //send the singleton object to the HTG Driver
    Twister2HTGInstance twister2HTGInstance = Twister2HTGInstance.getTwister2HTGInstance();
    twister2HTGInstance.setHtgJob(htgJob);
    twister2HTGInstance.setExecuteMessagesList(executeMessageList);
    twister2HTGInstance.setHtgSchedulerClassName(Twister2HTGScheduler.class.getName());

    //Setting the first graph resource requirements for the initial resource allocation
    twister2Job = Twister2Job.newBuilder()
        .setJobName(htgJob.getHtgJobname())
        .setWorkerClass(workerclassName)
        .setDriverClass(Twister2HTGDriver.class.getName())
        .addComputeResource(subGraph.getCpu(), subGraph.getRamMegaBytes(),
            subGraph.getDiskGigaBytes(), subGraph.getNumberOfInstances())
        .setConfig(jobConfig)
        .build();

    Twister2Submitter.submitJob(twister2Job, config);
  }
}