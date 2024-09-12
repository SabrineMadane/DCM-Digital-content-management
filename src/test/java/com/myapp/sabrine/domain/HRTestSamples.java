package com.myapp.sabrine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HRTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HR getHRSample1() {
        return new HR().id(1L).nom("nom1").prenom("prenom1").email("email1");
    }

    public static HR getHRSample2() {
        return new HR().id(2L).nom("nom2").prenom("prenom2").email("email2");
    }

    public static HR getHRRandomSampleGenerator() {
        return new HR()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
