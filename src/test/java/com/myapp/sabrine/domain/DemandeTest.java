package com.myapp.sabrine.domain;

import static com.myapp.sabrine.domain.DemandeTestSamples.*;
import static com.myapp.sabrine.domain.EntrepriseTestSamples.*;
import static com.myapp.sabrine.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demande.class);
        Demande demande1 = getDemandeSample1();
        Demande demande2 = new Demande();
        assertThat(demande1).isNotEqualTo(demande2);

        demande2.setId(demande1.getId());
        assertThat(demande1).isEqualTo(demande2);

        demande2 = getDemandeSample2();
        assertThat(demande1).isNotEqualTo(demande2);
    }

    @Test
    void utilisateurTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        demande.setUtilisateur(utilisateurBack);
        assertThat(demande.getUtilisateur()).isEqualTo(utilisateurBack);

        demande.utilisateur(null);
        assertThat(demande.getUtilisateur()).isNull();
    }

    @Test
    void entrepriseTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Entreprise entrepriseBack = getEntrepriseRandomSampleGenerator();

        demande.setEntreprise(entrepriseBack);
        assertThat(demande.getEntreprise()).isEqualTo(entrepriseBack);

        demande.entreprise(null);
        assertThat(demande.getEntreprise()).isNull();
    }
}
