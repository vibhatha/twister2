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
package edu.iu.dsc.tws.api.task.batch;

import edu.iu.dsc.tws.common.config.Config;
import edu.iu.dsc.tws.task.api.IMessage;
import edu.iu.dsc.tws.task.api.ISink;
import edu.iu.dsc.tws.task.api.TaskContext;

/**
 * Special task for collecting the output from tasks
 */
public class Collector implements ISink {
  @Override
  public boolean execute(IMessage message) {
    return false;
  }

  @Override
  public void prepare(Config cfg, TaskContext context) {

  }

  /**
   * get the collected valued
   */
  public Object get() {
    return null;
  }
}