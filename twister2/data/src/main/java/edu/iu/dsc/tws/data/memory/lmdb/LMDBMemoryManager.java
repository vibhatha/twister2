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
package edu.iu.dsc.tws.data.memory.lmdb;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.lmdbjava.CursorIterator;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;
import org.lmdbjava.KeyRange;
import org.lmdbjava.Txn;

import edu.iu.dsc.tws.data.fs.Path;
import edu.iu.dsc.tws.data.memory.AbstractMemoryManager;
import edu.iu.dsc.tws.data.memory.MemoryManagerContext;
import edu.iu.dsc.tws.data.memory.OperationMemoryManager;
import edu.iu.dsc.tws.data.memory.utils.DataMessageType;
import edu.iu.dsc.tws.data.utils.KryoMemorySerializer;
import edu.iu.dsc.tws.data.utils.MemoryDeserializer;

import static org.lmdbjava.DbiFlags.MDB_CREATE;
import static org.lmdbjava.Env.create;

/**
 * Memory Manger implementaion for LMDB Java
 * https://github.com/lmdbjava/lmdbjava
 */
public class LMDBMemoryManager extends AbstractMemoryManager {

  private static final Logger LOG = Logger.getLogger(LMDBMemoryManager.class.getName());

  /**
   * Path to keep the memory mapped file for the Memory manager
   */
  private Path lmdbDataPath;

  /**
   * The Memory Manager environment
   */
  private Env<ByteBuffer> env;

  /**
   * The Default Database for the Memory Manager
   * This can be used to store data that is not realated to any operation
   */
  private Dbi<ByteBuffer> db;

  private Map<Integer, Dbi<ByteBuffer>> dbMap;

  public LMDBMemoryManager(Path dataPath) {
    this.lmdbDataPath = dataPath;
    init();
  }

  @Override
  public boolean init() {
    if (lmdbDataPath == null || lmdbDataPath.isNullOrEmpty()) {
      lmdbDataPath = new Path(LMDBMemoryManagerContext.DEFAULT_FOLDER_PATH);
    }
    final File path = new File(lmdbDataPath.getPath());

    this.env = create()
        .setMapSize(LMDBMemoryManagerContext.MAP_SIZE_LIMIT)
        .setMaxDbs(LMDBMemoryManagerContext.MAX_DB_INSTANCES)
        .setMaxReaders(LMDBMemoryManagerContext.MAX_READERS)
        .open(path);

    // The database supports duplicate values for a single key
    db = env.openDbi(LMDBMemoryManagerContext.DB_NAME, MDB_CREATE);
    dbMap = new HashMap<Integer, Dbi<ByteBuffer>>();
    return true;
  }

  @Override
  public ByteBuffer get(int opID, ByteBuffer key) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return null;
    }
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    if (key.position() != 0) {
      key.flip();
    }

    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return null;
    }
    // details in LMDB for clarity
    // To fetch any data from LMDB we need a Txn. A Txn is very important in
    // LmdbJava because it offers ACID characteristics and internally holds a
    // read-only key buffer and read-only value buffer. These read-only buffers
    // are always the same two Java objects, but point to different LMDB-managed
    // memory as we use Dbi (and Cursor) methods. These read-only buffers remain
    // valid only until the Txn is released or the next Dbi or Cursor call. If
    // you need data afterwards, you should copy the bytes to your own buffer.
    //TODO: does the value returned from db.get and tnx.val() have the same data? need to check
    Txn<ByteBuffer> txn = env.txnRead();

    ByteBuffer result = currentDB.get(txn, key);
    txn.close();
    return result;
  }

  /*@Override
  public ByteBuffer get(int opID, byte[] key) {
    if (key.length > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return null;
    }

    final ByteBuffer keyBuffer = allocateDirect(key.length);
    keyBuffer.put(key);
    return get(opID, keyBuffer);
  }

  @Override
  public ByteBuffer get(int opID, long key) {
    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);
    return get(opID, keyBuffer);
  }*/

  @Override
  public ByteBuffer get(int opID, String key) {
    return get(opID, ByteBuffer.wrap(key.getBytes(MemoryManagerContext.DEFAULT_CHARSET)));
  }

  /*@Override
  public <T extends Serializable> ByteBuffer get(int opID, T key) {
    return null;
  }*/

  public ByteBuffer getAll(ByteBuffer key) {
    if (key.position() != 0) {
      key.flip();
    }

    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return null;
    }
    return null;
  }

  /*@Override
  public byte[] getBytes(int opID, byte[] key) {
    if (key.length > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return null;
    }

    final ByteBuffer keyBuffer = allocateDirect(key.length);
    keyBuffer.put(key);
    return getBytes(opID, keyBuffer);
  }

  @Override
  public byte[] getBytes(int opID, long key) {

    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);
    return getBytes(opID, key);
  }

  @Override
  public byte[] getBytes(int opID, String key) {
    return new byte[0];
  }

  @Override
  public <T extends Serializable> byte[] getBytes(int opID, T key) {
    return new byte[0];
  }

  @Override
  public byte[] getBytes(int opID, ByteBuffer key) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return null;
    }
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    if (key.position() != 0) {
      key.flip();
    }

    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return null;
    }

    Txn<ByteBuffer> txn = env.txnRead();
    final ByteBuffer found = currentDB.get(txn, key);
    byte[] results = new byte[found.limit()];
    found.get(results);
    return results;
  }*/

  @Override
  public boolean containsKey(int opID, ByteBuffer key) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return false;
    }
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }
    Txn<ByteBuffer> txn = env.txnRead();
    final ByteBuffer found = currentDB.get(txn, key);

    if (found == null) {
      return false;
    }
    return true;
  }

  /*@Override
  public boolean containsKey(int opID, byte[] key) {
    if (key.length > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }

    final ByteBuffer keyBuffer = allocateDirect(key.length);
    keyBuffer.put(key).flip();

    return containsKey(opID, keyBuffer);
  }

  @Override
  public boolean containsKey(int opID, long key) {
    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);

    return containsKey(opID, keyBuffer);
  }*/

  @Override
  public boolean containsKey(int opID, String key) {
    return containsKey(opID, ByteBuffer.wrap(key.getBytes(MemoryManagerContext.DEFAULT_CHARSET)));
  }

  /*@Override
  public <T extends Serializable> boolean containsKey(int opID, T key) {
    return false;
  }*/

  @Override
  public boolean append(int opID, ByteBuffer key, ByteBuffer value) {
    ByteBuffer results = get(opID, key);
    if (results == null) {
      return put(opID, key, value);
    }

    int capacity = results.limit() + value.limit();
    ByteBuffer appended = ByteBuffer.allocateDirect(capacity)
        .put(results)
        .put(value);
    return put(opID, key, appended);
  }

  /*@Override
  public boolean append(int opID, byte[] key, ByteBuffer value) {
    return false;
  }

  @Override
  public boolean append(int opID, long key, ByteBuffer value) {
    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);
    return append(opID, keyBuffer, value);
  }*/

  @Override
  public boolean append(int opID, String key, ByteBuffer value) {
    return append(opID, ByteBuffer.wrap(key.getBytes(MemoryManagerContext.DEFAULT_CHARSET)), value);
  }

  /*@Override
  public <T extends Serializable> boolean append(int opID, T key, ByteBuffer value) {
    return false;
  }*/

  /**
   * Insert key value pair into the
   *
   * @param key the key, must be unver 511 bytes because of limits in LMDB implementaion
   * @param value the value to be added
   * @return true if value was added, false otherwise
   */
  public boolean put(int opID, ByteBuffer key, ByteBuffer value) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return false;
    }
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    if (currentDB == null) {
      throw new RuntimeException("LMDB database has not been configured."
          + " Please initialize database");
    }

    if (key.position() != 0) {
      key.flip();
    }

    if (value.position() != 0) {
      value.flip();
    }

    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }
    currentDB.put(key, value);
    return true;
  }

  /*@Override
  public boolean put(int opID, byte[] key, byte[] value) {

    if (key.length > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }

    final ByteBuffer keyBuffer = allocateDirect(key.length);
    final ByteBuffer valBuffer = allocateDirect(value.length);
    keyBuffer.put(key);
    valBuffer.put(value);
    return put(opID, keyBuffer, valBuffer);
  }

  @Override
  public boolean put(int opID, byte[] key, ByteBuffer value) {

    if (key.length > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }

    final ByteBuffer keyBuffer = allocateDirect(key.length);
    keyBuffer.put(key);
    return put(opID, keyBuffer, value);
  }

  @Override
  public boolean put(int opID, long key, ByteBuffer value) {

    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);
    return put(opID, keyBuffer, value);
  }*/

  @Override
  public boolean put(int opID, String key, ByteBuffer value) {
    return put(opID, ByteBuffer.wrap(key.getBytes(MemoryManagerContext.DEFAULT_CHARSET)), value);
  }

  /*@Override
  public <T extends Serializable> boolean put(int opID, T key, ByteBuffer value) {
    return false;
  }

  @Override
  public boolean put(int opID, long key, byte[] value) {

    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    final ByteBuffer valBuffer = allocateDirect(value.length);
    keyBuffer.putLong(0, key);
    valBuffer.put(value);
    return put(opID, keyBuffer, valBuffer);
  }

  @Override
  public boolean put(int opID, String key, byte[] value) {
    return false;
  }

  @Override
  public <T extends Serializable> boolean put(int opID, T key, byte[] value) {
    return false;
  }*/

  @Override
  public boolean delete(int opID, ByteBuffer key) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return false;
    }
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    if (currentDB == null) {
      throw new RuntimeException("LMDB database has not been configured."
          + " Please initialize database");
    }

    if (key.position() != 0) {
      key.flip();
    }

    if (key.limit() > 511) {
      LOG.info("Key size lager than 511 bytes which is the limit for LMDB key values");
      return false;
    }

    return currentDB.delete(key);
  }

  /*@Override
  public boolean delete(int opID, byte[] key) {
    final ByteBuffer keyBuffer = allocateDirect(key.length);
    keyBuffer.put(key);
    return delete(opID, keyBuffer);
  }

  @Override
  public boolean delete(int opID, long key) {
    final ByteBuffer keyBuffer = allocateDirect(Long.BYTES);
    keyBuffer.putLong(0, key);
    return delete(opID, keyBuffer);
  }*/

  @Override
  public boolean delete(int opID, String key) {
    return delete(opID, ByteBuffer.wrap(key.getBytes(MemoryManagerContext.DEFAULT_CHARSET)));
  }

  /*@Override
  public <T extends Serializable> boolean delete(int opID, T key) {
    return false;
  }*/

  /**
   * At this level the method does not return an OperationMemoryManager since the implementaion
   * does not handle OperationMemoryManager's
   */
  @Override
  public OperationMemoryManager addOperation(int opID, DataMessageType messageType) {
    dbMap.put(opID, env.openDbi(String.valueOf(opID), MDB_CREATE));
    return new OperationMemoryManager(opID, messageType, this);
  }

  @Override
  public OperationMemoryManager addOperation(int opID, DataMessageType messageType,
                                             DataMessageType keyType) {
    dbMap.put(opID, env.openDbi(String.valueOf(opID), MDB_CREATE));
    return new OperationMemoryManager(opID, messageType, keyType, this);
  }

  @Override
  public boolean removeOperation(int opID) {
    //TODO: lmdb docs say that calling close is normally unnecessary; use with caution. need to
    // later check this to make sure it has been done the correct way
    dbMap.get(opID).close();
    dbMap.remove(opID);
    return true;
  }

  @Override
  public boolean flush(int opID, ByteBuffer key) {
    return false;
  }

  /*@Override
  public boolean flush(int opID, byte[] key) {
    return false;
  }

  @Override
  public boolean flush(int opID, long key) {
    return false;
  }*/

  @Override
  public boolean flush(int opID, String key) {
    return false;
  }

  /*@Override
  public <T extends Serializable> boolean flush(int opID, T key) {
    return false;
  }*/

  @Override
  public boolean close(int opID, ByteBuffer key) {
    return false;
  }

 /* @Override
  public boolean close(int opID, byte[] key) {
    return false;
  }

  @Override
  public boolean close(int opID, long key) {
    return false;
  }*/

  @Override
  public boolean close(int opID, String key) {
    return false;
  }

  /**
   * Returns an iterator that contains all the byte buffers for the given operation
   */
  @Override
  public Iterator<Object> getIterator(int opID, DataMessageType keyType,
                                      DataMessageType valueType,
                                      KryoMemorySerializer deSerializer) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return null;
    }
    List<Object> results = new ArrayList<>();
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    Txn<ByteBuffer> txn = env.txnRead();
    try (CursorIterator<ByteBuffer> it = currentDB.iterate(txn, KeyRange.all())) {
      for (final CursorIterator.KeyVal<ByteBuffer> kv : it.iterable()) {
        Object key = MemoryDeserializer.deserializeKey(kv.key(), keyType, deSerializer);
        Object value = MemoryDeserializer.deserializeValue(kv.val(), valueType, deSerializer);
        results.add(new ImmutablePair<>(key, value));
      }
    }
    txn.close();
    return results.iterator();
  }

  /**
   * Returns an iterator that contains all the byte buffers for the given operation
   * This method assumes that the keys are int's and that the do not need to be returned
   */
  @Override
  public Iterator<Object> getIterator(int opID, DataMessageType valueType,
                                      KryoMemorySerializer deSerializer) {
    if (!dbMap.containsKey(opID)) {
      LOG.info("The given operation does not have a corresponding store specified");
      return null;
    }
    List<Object> results = new ArrayList<>();
    Dbi<ByteBuffer> currentDB = dbMap.get(opID);
    Txn<ByteBuffer> txn = env.txnRead();
    try (CursorIterator<ByteBuffer> it = currentDB.iterate(txn, KeyRange.all())) {
      for (final CursorIterator.KeyVal<ByteBuffer> kv : it.iterable()) {
        Object value = MemoryDeserializer.deserializeValue(kv.val(), valueType, deSerializer);
        results.add(value);
      }
    }
    txn.close();
    return results.iterator();
  }

 /* @Override
  public <T extends Serializable> boolean close(int opID, T key) {
    return false;
  }*/

  public Path getLmdbDataPath() {
    return lmdbDataPath;
  }

  public void setLmdbDataPath(Path lmdbDataPath) {
    this.lmdbDataPath = lmdbDataPath;
  }


  public Env<ByteBuffer> getEnv() {
    return env;
  }

  public void setEnv(Env<ByteBuffer> env) {
    this.env = env;
  }

  public Dbi<ByteBuffer> getDb() {
    return db;
  }

  public void setDb(Dbi<ByteBuffer> db) {
    this.db = db;
  }
}
