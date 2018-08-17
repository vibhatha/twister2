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
package edu.iu.dsc.tws.examples.internal.task.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.iu.dsc.tws.api.JobConfig;
import edu.iu.dsc.tws.api.Twister2Submitter;
import edu.iu.dsc.tws.api.basic.job.BasicJob;
import edu.iu.dsc.tws.common.config.Config;
import edu.iu.dsc.tws.common.resource.WorkerComputeSpec;
import edu.iu.dsc.tws.common.resource.ZResourcePlan;
import edu.iu.dsc.tws.comms.core.TWSNetwork;
import edu.iu.dsc.tws.executor.api.ExecutionModel;
import edu.iu.dsc.tws.executor.api.ExecutionPlan;
import edu.iu.dsc.tws.executor.comm.tasks.batch.SinkBatchTask;
import edu.iu.dsc.tws.executor.comm.tasks.batch.SourceBatchTask;
import edu.iu.dsc.tws.executor.core.CommunicationOperationType;
import edu.iu.dsc.tws.executor.core.ExecutionPlanBuilder;
import edu.iu.dsc.tws.executor.threading.Executor;
import edu.iu.dsc.tws.rsched.core.ResourceAllocator;
import edu.iu.dsc.tws.rsched.core.SchedulerContext;
import edu.iu.dsc.tws.rsched.spi.container.IContainer;
import edu.iu.dsc.tws.task.api.IMessage;
import edu.iu.dsc.tws.task.api.TaskContext;
import edu.iu.dsc.tws.task.graph.DataFlowTaskGraph;
import edu.iu.dsc.tws.task.graph.GraphBuilder;
import edu.iu.dsc.tws.task.graph.OperationMode;
import edu.iu.dsc.tws.tsched.spi.scheduler.Worker;
import edu.iu.dsc.tws.tsched.spi.scheduler.WorkerPlan;
import edu.iu.dsc.tws.tsched.spi.taskschedule.TaskSchedulePlan;
import edu.iu.dsc.tws.tsched.streaming.roundrobin.RoundRobinTaskScheduler;

public class AllReduceBatchTask implements IContainer {
  @Override
  public void init(Config config, int id, ZResourcePlan resourcePlan) {
    GeneratorTask g = new GeneratorTask();
    RecevingTask r = new RecevingTask();
    OperationMode operationMode = OperationMode.BATCH;
    GraphBuilder builder = GraphBuilder.newBuilder();
    builder.addSource("source", g);
    builder.setParallelism("source", 4);
    builder.addSink("sink", r);
    builder.setParallelism("sink", 4);
    builder.connect("source", "sink", "all-reduce-edge",
        CommunicationOperationType.BATCH_ALLREDUCE);
    builder.operationMode(operationMode);
    DataFlowTaskGraph graph = builder.build();

    RoundRobinTaskScheduler roundRobinTaskScheduler = new RoundRobinTaskScheduler();
    roundRobinTaskScheduler.initialize(config);

    WorkerPlan workerPlan = createWorkerPlan(resourcePlan);
    TaskSchedulePlan taskSchedulePlan = roundRobinTaskScheduler.schedule(graph, workerPlan);

    TWSNetwork network = new TWSNetwork(config, resourcePlan.getThisId());
    ExecutionPlanBuilder executionPlanBuilder = new ExecutionPlanBuilder(resourcePlan, network);
    ExecutionPlan plan = executionPlanBuilder.build(config, graph, taskSchedulePlan);
    ExecutionModel executionModel = new ExecutionModel(ExecutionModel.SHARING);
    Executor executor = new Executor(config, executionModel, plan, network.getChannel(),
        operationMode);
    executor.execute();
  }

  private static class GeneratorTask extends SourceBatchTask {
    private static final long serialVersionUID = -254264903510284748L;
    private TaskContext ctx;
    private Config config;

    @Override
    public void run() {
      ctx.write("all-reduce-edge", "Hello");
    }

    @Override
    public void interrupt() {

    }


    @Override
    public void prepare(Config cfg, TaskContext context) {
      this.ctx = context;
    }
  }

  private static class RecevingTask extends SinkBatchTask {
    private static final long serialVersionUID = -254264903510284798L;
    private int count = 0;

    @Override
    public boolean execute(IMessage message) {

      System.out.println("Message AllReduced : " + message.getContent() + ", Count : " + count);

      count++;
      return true;
    }

    @Override
    public void prepare(Config cfg, TaskContext context) {

    }
  }

  public WorkerPlan createWorkerPlan(ZResourcePlan resourcePlan) {
    List<Worker> workers = new ArrayList<>();
    for (WorkerComputeSpec resource : resourcePlan.getContainers()) {
      Worker w = new Worker(resource.getId());
      workers.add(w);
    }

    return new WorkerPlan(workers);
  }

  public static void main(String[] args) {
    // first load the configurations from command line and config files
    Config config = ResourceAllocator.loadConfig(new HashMap<>());

    // build JobConfig
    HashMap<String, Object> configurations = new HashMap<>();
    configurations.put(SchedulerContext.THREADS_PER_WORKER, 8);

    // build JobConfig
    JobConfig jobConfig = new JobConfig();
    jobConfig.putAll(configurations);

    BasicJob.BasicJobBuilder jobBuilder = BasicJob.newBuilder();
    jobBuilder.setName("reduce-task-example");
    jobBuilder.setContainerClass(AllReduceBatchTask.class.getName());
    jobBuilder.setRequestResource(new WorkerComputeSpec(2, 1024), 4);
    jobBuilder.setConfig(jobConfig);

    // now submit the job
    Twister2Submitter.submitContainerJob(jobBuilder.build(), config);
  }

}