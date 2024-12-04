package com.ead.course.services;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

public interface LessonService {

    LessonModel save(LessonRecordDto lessonRecordDto, ModuleModel moduleModel);



}
