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
package edu.iu.dsc.tws.comms.dfw.io.types.primitive;

import java.nio.ByteBuffer;

import edu.iu.dsc.tws.comms.api.MessageType;
import edu.iu.dsc.tws.comms.api.MessageTypes;

public final class CharArrayPacker implements PrimitiveArrayPacker<char[]> {

  private static volatile CharArrayPacker instance;

  private CharArrayPacker() {
  }

  public static CharArrayPacker getInstance() {
    if (instance == null) {
      instance = new CharArrayPacker();
    }
    return instance;
  }

  @Override
  public MessageType<char[], char[]> getMessageType() {
    return MessageTypes.CHAR_ARRAY;
  }

  @Override
  public ByteBuffer addToBuffer(ByteBuffer byteBuffer, char[] data, int index) {
    return byteBuffer.putChar(data[index]);
  }

  @Override
  public ByteBuffer addToBuffer(ByteBuffer byteBuffer, int offset, char[] data, int index) {
    return byteBuffer.putChar(offset, data[index]);
  }

  @Override
  public void readFromBufferAndSet(ByteBuffer byteBuffer, int offset, char[] array, int index) {
    array[index] = byteBuffer.getChar(offset);
  }

  @Override
  public void readFromBufferAndSet(ByteBuffer byteBuffer, char[] array, int index) {
    array[index] = byteBuffer.getChar();
  }

  @Override
  public char[] wrapperForLength(int length) {
    return new char[length];
  }
}