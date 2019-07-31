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

package edu.iu.dsc.tws.api.tset.link.batch;

import edu.iu.dsc.tws.api.task.OperationNames;
import edu.iu.dsc.tws.api.task.graph.Edge;
import edu.iu.dsc.tws.api.tset.TSetUtils;
import edu.iu.dsc.tws.api.tset.env.BatchTSetEnvironment;
import edu.iu.dsc.tws.api.tset.fn.ReduceFunc;

public class ReduceTLink<T> extends BSingleLink<T> {
  private ReduceFunc<T> reduceFn;

  public ReduceTLink(BatchTSetEnvironment tSetEnv, ReduceFunc<T> rFn, int sourceParallelism) {
    super(tSetEnv, TSetUtils.generateName("reduce"), sourceParallelism, 1);
    this.reduceFn = rFn;
  }

  @Override
  public ReduceTLink<T> setName(String name) {
    rename(name);
    return this;
  }

  @Override
  public Edge getEdge() {
    return new Edge(getName(), OperationNames.REDUCE, getMessageType(), reduceFn);
  }

}
