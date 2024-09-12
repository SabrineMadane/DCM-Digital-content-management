package com.myapp.sabrine.service.mapper;

import com.myapp.sabrine.domain.Question;
import com.myapp.sabrine.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {}
