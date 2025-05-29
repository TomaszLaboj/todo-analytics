package com.example.todo_analytics.repository;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StatsEntity implements Serializable {

    public StatsEntity(TaskLabel label, int totalCount, int doneCount) {
        this.label = label;
        this.totalCount = totalCount;
        this.doneCount = doneCount;
    }

    public StatsEntity(Long id, TaskLabel label, int totalCount, int doneCount) {
        this.id = id;
        this.label = label;
        this.totalCount = totalCount;
        this.doneCount = doneCount;
    }

    public StatsEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Enumerated(EnumType.STRING)
    TaskLabel label;

    int totalCount;
    int doneCount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskLabel getLabel() {
        return label;
    }

    public void setLabel(TaskLabel label) {
        this.label = label;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(int doneCount) {
        this.doneCount = doneCount;
    }
}
