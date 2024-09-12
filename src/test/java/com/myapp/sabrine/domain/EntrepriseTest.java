package com.myapp.sabrine.domain;

import static com.myapp.sabrine.domain.EntrepriseTestSamples.*;
import static com.myapp.sabrine.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EntrepriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entreprise.class);
        Entreprise entreprise1 = getEntrepriseSample1();
        Entreprise entreprise2 = new Entreprise();
        assertThat(entreprise1).isNotEqualTo(entreprise2);

        entreprise2.setId(entreprise1.getId());
        assertThat(entreprise1).isEqualTo(entreprise2);

        entreprise2 = getEntrepriseSample2();
        assertThat(entreprise1).isNotEqualTo(entreprise2);
    }

    @Test
    void utilisateurTest() {
        Entreprise entreprise = getEntrepriseRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        entreprise.addUtilisateur(utilisateurBack);
        assertThat(entreprise.getUtilisateurs()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getEntreprisesDesirees()).containsOnly(entreprise);

        entreprise.removeUtilisateur(utilisateurBack);
        assertThat(entreprise.getUtilisateurs()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getEntreprisesDesirees()).doesNotContain(entreprise);

        entreprise.utilisateurs(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(entreprise.getUtilisateurs()).containsOnly(utilisateurBack);
        assertThat(utilisateurBack.getEntreprisesDesirees()).containsOnly(entreprise);

        entreprise.setUtilisateurs(new HashSet<>());
        assertThat(entreprise.getUtilisateurs()).doesNotContain(utilisateurBack);
        assertThat(utilisateurBack.getEntreprisesDesirees()).doesNotContain(entreprise);
    }
}
