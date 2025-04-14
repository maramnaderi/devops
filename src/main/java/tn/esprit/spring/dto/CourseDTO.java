package tn.esprit.spring.dto;
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


    public CourseDTO(Long numCourse, int level, TypeCourse typeCourse, Support support, Float price, int timeSlot) {
        this.numCourse = numCourse;
        this.level = level;
        this.typeCourse = typeCourse;
        this.support = support;
        this.price = price;
        this.timeSlot = timeSlot;

    }

    public CourseDTO() {

    }
}
