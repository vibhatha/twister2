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
package edu.iu.dsc.tws.examples.verification;

import java.util.Arrays;

public final class IntArrayWrapper implements Comparable<IntArrayWrapper> {

  private int[] array;

  private IntArrayWrapper(int[] array) {
    this.array = array;
  }

  public static IntArrayWrapper wrap(int[] array) {
    return new IntArrayWrapper(array);
  }

  public static IntArrayWrapper wrap(Object array) {
    return new IntArrayWrapper((int[]) array);
  }

  public int[] getArray() {
    return array;
  }

  public int getSize() {
    return this.array.length;
  }

  @Override
  public int compareTo(IntArrayWrapper o) {
    return Arrays.equals(array, o.array) ? 0 : -1;
  }

  @Override
  public String toString() {
    return Arrays.toString(this.array);
  }
}
