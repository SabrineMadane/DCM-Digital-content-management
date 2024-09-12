package com.myapp.sabrine.service.mapper;

import com.myapp.sabrine.domain.Entreprise;
import com.myapp.sabrine.domain.HR;
import com.myapp.sabrine.service.dto.EntrepriseDTO;
import com.myapp.sabrine.service.dto.HRDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HR} and its DTO {@link HRDTO}.
 */
@Mapper(componentModel = "spring")
public interface HRMapper extends EntityMapper<HRDTO, HR> {
    @Mapping(target = "entreprise", source = "entreprise", qualifiedByName = "entrepriseId")
    HRDTO toDto(HR s);

    @Named("entrepriseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EntrepriseDTO toDtoEntrepriseId(Entreprise entreprise);
}
