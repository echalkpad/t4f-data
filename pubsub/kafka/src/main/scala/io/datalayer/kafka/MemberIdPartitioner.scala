package io.datalayer.kafka

import kafka.producer.Partitioner

class MemberIdPartitioner extends Partitioner[MemberIdLocation] {

  def partition(data: MemberIdLocation, numPartitions: Int): Int = {
    (data.location.hashCode % numPartitions)
  }

}
