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
package edu.iu.dsc.tws.api.resource;

import edu.iu.dsc.tws.api.faulttolerance.FaultAcceptable;
import edu.iu.dsc.tws.proto.jobmaster.JobMasterAPI;

/**
 * An interface to receive worker failures in Twister2 jobs
 */
public interface IWorkerFailureListener {

  /**
   * let the listener know that a worker failed
   * @param workerID
   */
  void failed(int workerID);

  /**
   * let the listener know that previously failed worker rejoined the job
   * @param workerInfo
   */
  void restarted(JobMasterAPI.WorkerInfo workerInfo);

  /**
   * Register a fault acceptor
   * @param faultAcceptable a component capable of accepting a fault
   */
  default void registerFaultAcceptor(FaultAcceptable faultAcceptable) {
  }

  /**
   * Un-Register a fault acceptor
   * @param faultAcceptable a component capable of accepting a fault
   */
  default void unRegisterFaultAcceptor(FaultAcceptable faultAcceptable) {
  }
}
