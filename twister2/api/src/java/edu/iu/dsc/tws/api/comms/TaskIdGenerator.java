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
package edu.iu.dsc.tws.api.comms;

import java.util.HashSet;
import java.util.Set;

/**
 * Task ids
 */
public class TaskIdGenerator {
  private int current;

  public TaskIdGenerator(int start) {
    this.current = start;
  }

  public int nextId() {
    this.current++;
    return current;
  }

  public Set<Integer> nextId(int number) {
    Set<Integer> ids = new HashSet<>();
    for (int i = 0; i < number; i++) {
      current++;
      ids.add(current);
    }
    return ids;
  }
}
