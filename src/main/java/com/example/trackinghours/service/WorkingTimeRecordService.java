package com.example.trackinghours.service;

import com.example.trackinghours.entity.WorkingTimeRecord;

import java.util.List;

public interface WorkingTimeRecordService {
    List<WorkingTimeRecord> getAllWorkingTimeRecords();
    void addWorkingTimeRecord(WorkingTimeRecord record);
    void addWorkingTimeFromCSV(WorkingTimeRecord record);
    void updateWorkingTimeRecord(WorkingTimeRecord record);
    void deleteWorkingTimeRecord(int id);
}
