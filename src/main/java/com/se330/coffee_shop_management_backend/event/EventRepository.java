package com.se330.coffee_shop_management_backend.event;

import org.springframework.data.repository.ListCrudRepository;

public interface EventRepository extends ListCrudRepository<Event,Integer> {
}
