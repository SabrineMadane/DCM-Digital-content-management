package com.myapp.sabrine.service.mapper;

import com.myapp.sabrine.domain.Demande;
import com.myapp.sabrine.domain.Entreprise;
import com.myapp.sabrine.domain.Utilisateur;
import com.myapp.sabrine.service.dto.DemandeDTO;
import com.myapp.sabrine.service.dto.EntrepriseDTO;
import com.myapp.sabrine.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Demande} and its DTO {@link DemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeMapper extends EntityMapper<DemandeDTO, Demande> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    @Mapping(target = "entreprise", source = "entreprise", qualifiedByName = "entrepriseId")
    DemandeDTO toDto(Demande s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);

    @Named("entrepriseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EntrepriseDTO toDtoEntrepriseId(Entreprise entreprise);
}
