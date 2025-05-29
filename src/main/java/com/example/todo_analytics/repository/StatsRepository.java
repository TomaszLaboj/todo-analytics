package com.example.todo_analytics.repository;


import org.springframework.data.repository.CrudRepository;


public interface StatsRepository extends CrudRepository<StatsEntity, Long> {

    StatsEntity findById(long id);
}
