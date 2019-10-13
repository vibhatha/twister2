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
package edu.iu.dsc.tws.tset.ops;

import java.util.Set;

import edu.iu.dsc.tws.api.compute.TaskContext;
import edu.iu.dsc.tws.api.compute.nodes.ISource;
import edu.iu.dsc.tws.api.config.Config;
import edu.iu.dsc.tws.api.tset.fn.SourceFunc;
import edu.iu.dsc.tws.tset.sets.BaseTSet;

public class SourceOp<T> extends BaseOp implements ISource {
  private MultiEdgeOpAdapter multiEdgeOpAdapter;
  private SourceFunc<T> source;

  public SourceOp() {

  }

  public SourceOp(SourceFunc<T> src, BaseTSet originTSet, Set<String> receivables) {
    super(originTSet, receivables);
    this.source = src;
  }

  @Override
  public void execute() {
    if (source.hasNext()) {
      multiEdgeOpAdapter.writeToEdges(source.next());
    } else {
      multiEdgeOpAdapter.writeEndToEdges();
    }
  }

  @Override
  public void prepare(Config cfg, TaskContext ctx) {
    this.multiEdgeOpAdapter = new MultiEdgeOpAdapter(ctx);

    this.updateTSetContext(cfg, ctx);

    this.source.prepare(gettSetContext());
  }
}
