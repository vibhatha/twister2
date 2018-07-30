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
package edu.iu.dsc.tws.connectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iu.dsc.tws.common.config.Config;
import edu.iu.dsc.tws.connectors.config.KafkaProducerConfig;
import edu.iu.dsc.tws.task.api.IMessage;
import edu.iu.dsc.tws.task.api.SinkTask;
import edu.iu.dsc.tws.task.api.TaskContext;

public class TwsKafkaProducer<T> extends SinkTask {
  private static final long serialVersionUID = -264264120110286749L;
  private static Logger log = LoggerFactory.getLogger(TwsKafkaProducer.class);
  private Properties kafkaConfigs;
  private int myIndex;
  private int worldSize;
  private List<String> listOfTopics = null;
  private Producer<String, String> producer;
  private KafkaPartitionFinder kafkaPartitionFinder;
  private KafkaTopicDescription topicDescription;
  private List<TopicPartition> topicPartitions;
  @Override
  public void execute(IMessage message) {
    log.info("Recieved message {}", message.getContent());
//    if (this.singleTopic == null) {
//      for (String topic : this.listOfTopics) {
//        log.info("Producing to kafka message : {} , Topic : {}", message.getContent(), topic);
//        producer.send(new ProducerRecord<String, String>(topic,
//            message.getContent().toString(),
//            message.getContent().toString()));
//      }
//    } else {
//      log.info("Producing to kafka message : {} , Topic : {}", message.getContent(), singleTopic);
//      producer.send(new ProducerRecord<String, String>(singleTopic,
//          message.getContent().toString(),
//          message.getContent().toString()));
//    }
    if (topicPartitions.isEmpty()) {
      log.info("No partition found for given topic(s)");
    } else {
      for (TopicPartition topicPartition : topicPartitions) {
        log.info("Producing to kafka, Message : {} , Topic : {}, Partition : {}",
            message.getContent(), topicPartition.topic(), topicPartition.partition());
        producer.send(new ProducerRecord<String, String>(topicPartition.topic(),
            topicPartition.partition(),
            message.getContent().toString(),
            message.getContent().toString()));
      }
    }

  }

  @Override
  public void prepare(Config cfg, TaskContext context) {
    this.myIndex = cfg.getIntegerValue("twister2.container.id", 0);
    this.worldSize = context.getParallelism();
    log.info("myID : {} , worldSize : {} ", myIndex, worldSize);
    this.topicDescription = new KafkaTopicDescription(listOfTopics);
    this.kafkaPartitionFinder = new KafkaPartitionFinder(
        this.kafkaConfigs, worldSize, myIndex, topicDescription);
    this.topicPartitions = kafkaPartitionFinder.getRelevantPartitions();
    this.producer = new KafkaProducer<String, String>(this.kafkaConfigs);

  }
  public TwsKafkaProducer(
      List<String> topics,
      List<String> servers
  ) {
    this.kafkaConfigs = createKafkaConfig(servers);
    this.listOfTopics = topics;

  }

  public TwsKafkaProducer(
      String singletopic,
      List<String> servers
  ) {
    this.kafkaConfigs = createKafkaConfig(servers);
    this.listOfTopics = new ArrayList<>();
    listOfTopics.add(singletopic);
  }

  private Properties createKafkaConfig(List<String> servers) {
    return KafkaProducerConfig.getConfig(servers);
  }

  public Properties setProperty(Properties newProps) {
    this.kafkaConfigs = KafkaProducerConfig.setProps(kafkaConfigs, newProps);
    return kafkaConfigs;
  }
  public Properties getKafkaConfigs() {
    return kafkaConfigs;
  }


  public void setKafkaConfigs(Properties kafkaConfigs) {
    this.kafkaConfigs = kafkaConfigs;
  }

  public TwsKafkaProducer() {

  }

}