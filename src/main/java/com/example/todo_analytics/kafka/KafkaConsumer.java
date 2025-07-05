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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumer {

    StatsRepository statsRepository;

    @Autowired
    public KafkaConsumer(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(id = "created", groupId = "todo-statistics", topics = {"created"})
    public void listenCreated(String in) throws JsonProcessingException {
        ToDoItem toDoItem = mapper.readValue(in, ToDoItem.class);
        created(toDoItem);
    }

	@KafkaListener(id = "updated", groupId = "todo-statistics", topics = { "updated"})
	public void listenUpdated(String in) throws JsonProcessingException {
        ToDoItemUpdated toDoItemUpdated = mapper.readValue(in, ToDoItemUpdated.class);
        ToDoItem original = toDoItemUpdated.original;
        ToDoItem updated = toDoItemUpdated.updated;
        if (original.getLabel() != updated.getLabel()) {
            deleted(original);
            created(updated);
        }
	};

    @KafkaListener(id = "deleted", groupId = "todo-statistics", topics = {"deleted"})
    public void listenDeleted(String in) throws JsonProcessingException {
        ToDoItem toDoItem = mapper.readValue(in, ToDoItem.class);
        deleted(toDoItem);
    }

    public StatsEntity created(ToDoItem toDoItem) {
        Optional<StatsEntity> statsEntity = statsRepository.findById(checkId(toDoItem.getLabel()));
        if (statsEntity.isPresent()) {
            StatsEntity stats = statsEntity.get();
            stats.setTotalCount(stats.getTotalCount() + 1);
            return statsRepository.save(stats);
        }
        //default return seems to be not saving to db if the db is empty
        return statsRepository.save(new StatsEntity(checkId(toDoItem.getLabel()),toDoItem.getLabel(),0,0)) ;
    };
    public StatsEntity deleted(ToDoItem toDoItem) {
        Optional<StatsEntity> statsEntity = statsRepository.findById(checkId(toDoItem.getLabel()));
        if (statsEntity.isPresent()) {
            StatsEntity stats = statsEntity.get();
            if (stats.getTotalCount() > 0) {
                stats.setTotalCount(stats.getTotalCount() -1);
            }
            return statsRepository.save(stats);
        }
        return null;
    };

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
