package com.boiis.todo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task")
public class Task {
    @Id
    private String id;
    private String user;
    private String heading;
    private String desc;
    private Status status;
    private Date reminder;
    private Date deadline;
    private String progressReport;

}
