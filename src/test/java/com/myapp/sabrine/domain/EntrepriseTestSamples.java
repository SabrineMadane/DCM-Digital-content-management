package com.myapp.sabrine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntrepriseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Entreprise getEntrepriseSample1() {
        return new Entreprise().id(1L).secteur("secteur1").focus("focus1").logo("logo1").profilLinkedIn("profilLinkedIn1");
    }

    public static Entreprise getEntrepriseSample2() {
        return new Entreprise().id(2L).secteur("secteur2").focus("focus2").logo("logo2").profilLinkedIn("profilLinkedIn2");
    }

    public static Entreprise getEntrepriseRandomSampleGenerator() {
        return new Entreprise()
            .id(longCount.incrementAndGet())
            .secteur(UUID.randomUUID().toString())
            .focus(UUID.randomUUID().toString())
            .logo(UUID.randomUUID().toString())
            .profilLinkedIn(UUID.randomUUID().toString());
    }
}
