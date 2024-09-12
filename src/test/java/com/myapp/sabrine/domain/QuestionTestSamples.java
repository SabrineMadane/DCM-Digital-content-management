package com.myapp.sabrine.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question().id(1L).contenu("contenu1").categorie("categorie1").niveau(1);
    }

    public static Question getQuestionSample2() {
        return new Question().id(2L).contenu("contenu2").categorie("categorie2").niveau(2);
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .contenu(UUID.randomUUID().toString())
            .categorie(UUID.randomUUID().toString())
            .niveau(intCount.incrementAndGet());
    }
}
