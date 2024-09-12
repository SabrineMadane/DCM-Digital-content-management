package com.myapp.sabrine.service.mapper;

import com.myapp.sabrine.domain.Entreprise;
import com.myapp.sabrine.domain.Utilisateur;
import com.myapp.sabrine.service.dto.EntrepriseDTO;
import com.myapp.sabrine.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "entreprisesDesirees", source = "entreprisesDesirees", qualifiedByName = "entrepriseIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "removeEntreprisesDesirees", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("entrepriseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EntrepriseDTO toDtoEntrepriseId(Entreprise entreprise);

    @Named("entrepriseIdSet")
    default Set<EntrepriseDTO> toDtoEntrepriseIdSet(Set<Entreprise> entreprise) {
        return entreprise.stream().map(this::toDtoEntrepriseId).collect(Collectors.toSet());
    }
}
