package tn.esprit.spring.DTO;
import lombok.Data;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
@Data
public class CourseDTO {
    Long numCourse;
    int level;
    TypeCourse typeCourse;
    Support support;
    Float price;
    int timeSlot;
}
