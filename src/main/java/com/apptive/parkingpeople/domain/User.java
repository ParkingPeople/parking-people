package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private AuthType auth_type;

    private String username;

    private int point;

    @OneToMany(mappedBy = "submitter")
    List<PhotoSubmission> submissions;

    @OneToMany(mappedBy = "user")
    List<PointHistory> point_history;

}
