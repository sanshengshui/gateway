package com.aiyolo.repository;

import com.aiyolo.entity.Channel;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Long> {

    Channel findOneByName(String name);

}
