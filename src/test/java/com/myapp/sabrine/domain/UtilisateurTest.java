package com.myapp.sabrine.domain;

import static com.myapp.sabrine.domain.EntrepriseTestSamples.*;
import static com.myapp.sabrine.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void entreprisesDesireesTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Entreprise entrepriseBack = getEntrepriseRandomSampleGenerator();

        utilisateur.addEntreprisesDesirees(entrepriseBack);
        assertThat(utilisateur.getEntreprisesDesirees()).containsOnly(entrepriseBack);

        utilisateur.removeEntreprisesDesirees(entrepriseBack);
        assertThat(utilisateur.getEntreprisesDesirees()).doesNotContain(entrepriseBack);

        utilisateur.entreprisesDesirees(new HashSet<>(Set.of(entrepriseBack)));
        assertThat(utilisateur.getEntreprisesDesirees()).containsOnly(entrepriseBack);

        utilisateur.setEntreprisesDesirees(new HashSet<>());
        assertThat(utilisateur.getEntreprisesDesirees()).doesNotContain(entrepriseBack);
    }
}
