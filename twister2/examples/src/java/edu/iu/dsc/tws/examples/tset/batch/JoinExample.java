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
package edu.iu.dsc.tws.examples.tset.batch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import edu.iu.dsc.tws.api.JobConfig;
import edu.iu.dsc.tws.api.comms.CommunicationContext;
import edu.iu.dsc.tws.api.comms.structs.JoinedTuple;
import edu.iu.dsc.tws.api.comms.structs.Tuple;
import edu.iu.dsc.tws.api.config.Config;
import edu.iu.dsc.tws.api.tset.env.BatchTSetEnvironment;
import edu.iu.dsc.tws.api.tset.link.batch.BatchTLink;
import edu.iu.dsc.tws.api.tset.sets.batch.KeyedTSet;
import edu.iu.dsc.tws.api.tset.sets.batch.SourceTSet;
import edu.iu.dsc.tws.rsched.core.ResourceAllocator;

public class JoinExample extends BatchTsetExample {
  private static final Logger LOG = Logger.getLogger(JoinExample.class.getName());

  @Override
  public void execute(BatchTSetEnvironment env) {
    int para = 1;
    SourceTSet<Integer> src0 = dummySource(env, COUNT, para);
    KeyedTSet<Integer, Integer> left = src0.mapToTuple(i -> new Tuple<>(i % 2, i));

    SourceTSet<Integer> src1 = dummySource(env, COUNT, para);
    KeyedTSet<Integer, Integer> right = src1.mapToTuple(i -> new Tuple<>(i % 2, i));

    BatchTLink<Iterator<JoinedTuple<Integer, Integer, Integer>>,
        JoinedTuple<Integer, Integer, Integer>> join =
        left.join(right, CommunicationContext.JoinType.FULL_OUTER);

    join.forEach(t -> LOG.info("out" + t.toString()));
  }

  public static void main(String[] args) {
    Config config = ResourceAllocator.loadConfig(new HashMap<>());

    JobConfig jobConfig = new JobConfig();
    BatchTsetExample.submitJob(config, PARALLELISM, jobConfig, JoinExample.class.getName());
  }
}
