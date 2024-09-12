package com.myapp.sabrine.domain;

import static com.myapp.sabrine.domain.EntrepriseTestSamples.*;
import static com.myapp.sabrine.domain.HRTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.sabrine.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HRTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HR.class);
        HR hR1 = getHRSample1();
        HR hR2 = new HR();
        assertThat(hR1).isNotEqualTo(hR2);

        hR2.setId(hR1.getId());
        assertThat(hR1).isEqualTo(hR2);

        hR2 = getHRSample2();
        assertThat(hR1).isNotEqualTo(hR2);
    }

    @Test
    void entrepriseTest() {
        HR hR = getHRRandomSampleGenerator();
        Entreprise entrepriseBack = getEntrepriseRandomSampleGenerator();

        hR.setEntreprise(entrepriseBack);
        assertThat(hR.getEntreprise()).isEqualTo(entrepriseBack);

        hR.entreprise(null);
        assertThat(hR.getEntreprise()).isNull();
    }
}
