package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {

        courseUserModel = courseUserRepository.save(courseUserModel);
        //send to other MS

        return courseUserModel;
    }
}
