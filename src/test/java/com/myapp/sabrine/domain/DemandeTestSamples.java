package com.myapp.sabrine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DemandeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Demande getDemandeSample1() {
        return new Demande().id(1L).etat("etat1");
    }

    public static Demande getDemandeSample2() {
        return new Demande().id(2L).etat("etat2");
    }

    public static Demande getDemandeRandomSampleGenerator() {
        return new Demande().id(longCount.incrementAndGet()).etat(UUID.randomUUID().toString());
    }
}
