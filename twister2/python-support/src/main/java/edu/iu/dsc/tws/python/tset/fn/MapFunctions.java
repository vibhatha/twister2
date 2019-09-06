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
package edu.iu.dsc.tws.python.tset.fn;

import edu.iu.dsc.tws.api.tset.fn.MapFunc;
import edu.iu.dsc.tws.python.processors.PythonLambdaProcessor;

import java.io.Serializable;

public class MapFunctions extends TFunc<MapFunc> {

  private static final MapFunctions INSTANCE = new MapFunctions();

  private MapFunctions() {

  }

  static MapFunctions getInstance() {
    return INSTANCE;
  }

  static class MapFuncImpl implements MapFunc, Serializable {

    private PythonLambdaProcessor lambdaProcessor;

    MapFuncImpl(PythonLambdaProcessor lambdaProcessor) {
      this.lambdaProcessor = lambdaProcessor;
    }

    @Override
    public Object map(Object input) {
      return lambdaProcessor.invoke(input);
    }
  }

  @Override
  public MapFunc build(byte[] pyBinary) {
    final PythonLambdaProcessor lambda = new PythonLambdaProcessor(pyBinary);
    return new MapFuncImpl(lambda);
  }
}
