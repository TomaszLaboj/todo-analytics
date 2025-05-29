package com.example.todo_analytics.kafka;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import com.example.todo_analytics.ToDoItem;
import com.example.todo_analytics.ToDoItemUpdated;
import com.example.todo_analytics.repository.StatsEntity;
import com.example.todo_analytics.repository.StatsRepository;
import com.example.todo_analytics.repository.TaskLabel;

@Component
public class KafkaConsumer {

    StatsRepository statsRepository;

    @Autowired
    public KafkaConsumer(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @KafkaListener(id = "created", groupId = "todo-statistics", topics = {"created"})
    public StatsEntity listenCreated(ToDoItem in) {

        Optional<StatsEntity> statsEntity = statsRepository.findById(checkId(in.getLabel()));
        if (statsEntity.isPresent()) {
            StatsEntity stats = statsEntity.get();
            stats.setTotalCount(stats.getTotalCount() + 1);
            return statsRepository.save(stats);
        }
        //default return seems to be not saving to db if the db is empty
        return statsRepository.save(new StatsEntity(checkId(in.getLabel()),in.getLabel(),0,0)) ;
    }

	@KafkaListener(id = "updated", groupId = "todo-statistics", topics = { "updated"})
	public void listenUpdated(ToDoItemUpdated in) {
		System.out.println(in);
	}

    @KafkaListener(id = "deleted", groupId = "todo-statistics", topics = {"deleted"})
    public StatsEntity listenDeleted(ToDoItem in) {
        Optional<StatsEntity> statsEntity = statsRepository.findById(checkId(in.getLabel()));
        if (statsEntity.isPresent()) {
            StatsEntity stats = statsEntity.get();
            if (stats.getTotalCount() > 0) {
                stats.setTotalCount(stats.getTotalCount() -1);
            }
            return statsRepository.save(stats);
        }
        return null;
    }

    Long checkId(TaskLabel label) {
        Long id = 1L;
        switch (label) {
            case TaskLabel.RED:
                id = 1L;
                break;
            case TaskLabel.GREEN:
                id = 2L;
                break;
            case TaskLabel.BLUE:
                id = 3L;
                break;
        }
        return id;
    }
}
