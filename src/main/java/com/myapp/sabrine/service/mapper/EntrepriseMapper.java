package com.myapp.sabrine.service.mapper;

import com.myapp.sabrine.domain.Entreprise;
import com.myapp.sabrine.domain.Utilisateur;
import com.myapp.sabrine.service.dto.EntrepriseDTO;
import com.myapp.sabrine.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Entreprise} and its DTO {@link EntrepriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntrepriseMapper extends EntityMapper<EntrepriseDTO, Entreprise> {
    @Mapping(target = "utilisateurs", source = "utilisateurs", qualifiedByName = "utilisateurIdSet")
    EntrepriseDTO toDto(Entreprise s);

    @Mapping(target = "utilisateurs", ignore = true)
    @Mapping(target = "removeUtilisateur", ignore = true)
    Entreprise toEntity(EntrepriseDTO entrepriseDTO);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("utilisateurIdSet")
    default Set<UtilisateurDTO> toDtoUtilisateurIdSet(Set<Utilisateur> utilisateur) {
        return utilisateur.stream().map(this::toDtoUtilisateurId).collect(Collectors.toSet());
    }
}
