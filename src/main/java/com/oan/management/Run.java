package com.oan.management;

import com.oan.management.model.Task;
import com.oan.management.model.User;
import com.oan.management.repository.TaskRepository;
import com.oan.management.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class Run {

    public static void main(String[] args) {

        SpringApplication.run(Run.class, args);

        /**
        ConfigurableApplicationContext configurableApplicationContext =
        SpringApplication.run(Run.class, args);

        UserRepository userRepository = configurableApplicationContext.getBean(UserRepository.class);
        User user = userRepository.findOne(3L);

        TaskRepository tr = configurableApplicationContext.getBean(TaskRepository.class);
        Task task = new Task(user, "description", Date.valueOf(LocalDate.of(2019,1,21)), false);
        tr.save(task);
        **/
    }
}
