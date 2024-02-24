package fr.epita.assistants.data.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course_model")
public class CourseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    public List<StudentModel> students;

    @ElementCollection
    @CollectionTable(name = "course_model_tags", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "tag")
    public List<String> tags;
}