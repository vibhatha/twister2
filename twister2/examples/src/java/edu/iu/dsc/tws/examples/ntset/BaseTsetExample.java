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
package edu.iu.dsc.tws.examples.ntset;

import java.io.Serializable;

import edu.iu.dsc.tws.api.JobConfig;
import edu.iu.dsc.tws.api.Twister2Job;
import edu.iu.dsc.tws.api.config.Config;
import edu.iu.dsc.tws.api.task.graph.OperationMode;
import edu.iu.dsc.tws.api.tset.TSetEnvironment;
import edu.iu.dsc.tws.api.tset.fn.Source;
import edu.iu.dsc.tws.api.tset.sets.BatchSourceTSet;
import edu.iu.dsc.tws.api.tset.worker.TSetIWorker;
import edu.iu.dsc.tws.rsched.job.Twister2Submitter;

public abstract class BaseTsetExample implements TSetIWorker, Serializable {
  static final int COUNT = 5;
  static final int PARALLELISM = 2;

  BatchSourceTSet<Integer> dummySource(TSetEnvironment env, int count, int parallel) {
    return env.createBatchSource(new Source<Integer>() {
      private int c = 0;

      @Override
      public boolean hasNext() {
        return c < count;
      }

      @Override
      public Integer next() {
        return c++;
      }
    }, parallel);
  }

  public static void submitJob(Config config, int containers, JobConfig jobConfig, String clazz) {
    Twister2Job twister2Job;
    twister2Job = Twister2Job.newBuilder()
        .setJobName(clazz)
        .setWorkerClass(clazz)
        .addComputeResource(1, 512, containers)
        .setConfig(jobConfig)
        .build();
    // now submit the job
    Twister2Submitter.submitJob(twister2Job, config);
  }

  @Override
  public OperationMode getOperationMode() {
    return OperationMode.BATCH;
  }
}
