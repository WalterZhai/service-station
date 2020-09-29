package com.cimctht.servicestation.common.schedule;

import com.cimctht.servicestation.user.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {

    @Autowired
    SyncService syncService;

    @Scheduled(cron="0 0 12 * * ?")
    public void syncEmployee() {
        syncService.syncEmployee();
    }

    @Scheduled(cron="0 0 11 * * ?")
    public void syncDepart() {
        syncService.syncDepart();
    }

}
