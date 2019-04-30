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
package edu.iu.dsc.tws.examples.task.streaming.windowing;

import java.util.List;
import java.util.logging.Logger;

import edu.iu.dsc.tws.api.task.TaskGraphBuilder;
import edu.iu.dsc.tws.data.api.DataType;
import edu.iu.dsc.tws.examples.task.BenchTaskWorker;
import edu.iu.dsc.tws.task.api.window.BaseWindowSource;
import edu.iu.dsc.tws.task.api.window.api.IWindowMessage;
import edu.iu.dsc.tws.task.api.window.config.TumblingCountWindow;
import edu.iu.dsc.tws.task.api.window.core.BaseWindowedSink;

public class STWindowCustomExample extends BenchTaskWorker {

  private static final Logger LOG = Logger.getLogger(STWindowCustomExample.class.getName());

  @Override
  public TaskGraphBuilder buildTaskGraph() {
    List<Integer> taskStages = jobParameters.getTaskStages();
    int sourceParallelism = taskStages.get(0);
    int sinkParallelism = taskStages.get(1);

    String edge = "edge";
    BaseWindowSource g = new SourceWindowTask(edge);

    BaseWindowedSink dw = new DirectCustomWindowReceiver()
        .withWindow(TumblingCountWindow.of(5));
    taskGraphBuilder.addSource(SOURCE, g, sourceParallelism);
    computeConnection = taskGraphBuilder.addSink(SINK, dw, sinkParallelism);
    computeConnection.direct(SOURCE, edge, DataType.INTEGER);

    return taskGraphBuilder;
  }

  protected static class DirectCustomWindowReceiver extends BaseWindowedSink<int[]> {

    public DirectCustomWindowReceiver() {
    }

    @Override
    public IWindowMessage<int[]> execute(IWindowMessage<int[]> windowMessage) {
      LOG.info(String.format("Items : %d ", windowMessage.getWindow().size()));
      return windowMessage;
    }
  }
}
