package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validations.CourseValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/courses")
public class CourseController {

    final CourseService courseService;
    final CourseValidator courseValidator;

    public CourseController(CourseService courseService, CourseValidator courseValidator) {
        this.courseService = courseService;
        this.courseValidator = courseValidator;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseRecordDto courseRecordDto, Errors errors){

        log.debug("POST saveCourse courseRecordDto received {}", courseRecordDto);
        courseValidator.validate(courseRecordDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseRecordDto));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           Pageable pageable,
                                                           @RequestParam(required = false) UUID userId){
        Page<CourseModel> courseModelPage = (userId != null)
                ? courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable)
                : courseService.findAll(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(courseModelPage);
    }

    @GetMapping(path = "/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId")UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findById(courseId).get());
    }

    @DeleteMapping(path = "/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId")UUID courseId){
        log.debug("DELETE deleteCourse courseId received {}", courseId);
        courseService.delete(courseService.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
    }

    @PutMapping(path = "/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId")UUID courseId,
                                               @RequestBody @Valid CourseRecordDto courseRecordDto){
        log.debug("PUT updateCourse courseId received {}", courseId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(courseService.update(courseRecordDto, courseService.findById(courseId).get()));
    }

}
