package com.roadnet.config;

import com.roadnet.model.Preference;
import com.roadnet.model.User;
import com.roadnet.model.enums.Gender;
import com.roadnet.model.enums.GeoScope;
import com.roadnet.model.enums.Intention;
import com.roadnet.model.enums.MaritalStatus;
import com.roadnet.repository.PreferenceRepository;
import com.roadnet.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String SEED_PASSWORD = "SeedPass123!";

    private final UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      PreferenceRepository preferenceRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.preferenceRepository = preferenceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Seed data skipped: users already exist ({})", userRepository.count());
            return;
        }

        seed("amina.kane@example.com", "Amina Kane", LocalDate.of(1998, 4, 12), Gender.FEMALE,
                "Senegal", "Dakar", List.of("French", "Wolof", "English"), MaritalStatus.SINGLE,
                "Architect", "Passionate about urban design and community spaces.",
                List.of(Intention.MARRIAGE, Intention.SERIOUS_RELATIONSHIP),
                GeoScope.DIASPORA, List.of("architecture", "photography", "travel"), List.of("non-smoker", "active"));

        seed("david.okanlawon@example.com", "David Okanlawon", LocalDate.of(1995, 9, 30), Gender.MALE,
                "Nigeria", "Lagos", List.of("English", "Yoruba"), MaritalStatus.SINGLE,
                "Software Engineer", "Building products that connect people across borders.",
                List.of(Intention.DIASPORA_CONNECTION, Intention.CULTURAL_EXCHANGE),
                GeoScope.GLOBAL, List.of("coding", "football", "music"), List.of("active", "early-bird"));

        seed("sofia.moretti@example.com", "Sofia Moretti", LocalDate.of(2000, 1, 5), Gender.FEMALE,
                "Italy", "Milan", List.of("Italian", "English", "Spanish"), MaritalStatus.SINGLE,
                "Designer", "Lifelong learner curious about other cultures.",
                List.of(Intention.CULTURAL_EXCHANGE, Intention.FRIENDSHIP),
                GeoScope.CROSS_BORDER, List.of("design", "art", "cooking"), List.of("vegetarian", "creative"));

        seed("james.otieno@example.com", "James Otieno", LocalDate.of(1991, 7, 22), Gender.MALE,
                "Kenya", "Nairobi", List.of("English", "Swahili"), MaritalStatus.DIVORCED,
                "Entrepreneur", "Founder of two startups. Looking to settle down.",
                List.of(Intention.SERIOUS_RELATIONSHIP, Intention.COMPANIONSHIP),
                GeoScope.REGIONAL, List.of("business", "hiking", "books"), List.of("non-smoker", "ambitious"));

        seed("mei.tanaka@example.com", "Mei Tanaka", LocalDate.of(1997, 12, 1), Gender.FEMALE,
                "Japan", "Tokyo", List.of("Japanese", "English", "Korean"), MaritalStatus.SINGLE,
                "Translator", "Love connecting cultures through language.",
                List.of(Intention.CULTURAL_EXCHANGE, Intention.SHARED_EXPERIENCES),
                GeoScope.INTERCONTINENTAL, List.of("languages", "cinema", "yoga"), List.of("active", "mindful"));

        seed("kwame.mensah@example.com", "Kwame Mensah", LocalDate.of(1993, 3, 18), Gender.MALE,
                "Ghana", "Accra", List.of("English", "Twi", "French"), MaritalStatus.SINGLE,
                "Journalist", "Telling stories that matter across the continent.",
                List.of(Intention.DIASPORA_CONNECTION, Intention.SERIOUS_RELATIONSHIP),
                GeoScope.DIASPORA, List.of("writing", "politics", "travel"), List.of("curious", "outgoing"));

        seed("elena.silva@example.com", "Elena Silva", LocalDate.of(1994, 6, 8), Gender.FEMALE,
                "Brazil", "Sao Paulo", List.of("Portuguese", "English", "Spanish"), MaritalStatus.SINGLE,
                "Doctor", "Emergency physician who loves salsa and the outdoors.",
                List.of(Intention.DATING, Intention.FRIENDSHIP),
                GeoScope.LOCAL, List.of("dance", "running", "medicine"), List.of("active", "social"));

        seed("ravi.sharma@example.com", "Ravi Sharma", LocalDate.of(1990, 11, 14), Gender.MALE,
                "India", "Mumbai", List.of("Hindi", "English", "Gujarati"), MaritalStatus.SINGLE,
                "Data Scientist", "Long-distance relationships are built on trust and communication.",
                List.of(Intention.LONG_DISTANCE, Intention.SHARED_EXPERIENCES),
                GeoScope.GLOBAL, List.of("data", "chess", "meditation"), List.of("introverted", "thoughtful"));

        log.info("Seeded {} sample users (password: {})", userRepository.count(), SEED_PASSWORD);
    }

    private void seed(String email, String displayName, LocalDate dob, Gender gender,
                      String country, String city, List<String> languages, MaritalStatus maritalStatus,
                      String profession, String bio,
                      List<Intention> intentions, GeoScope geoScope,
                      List<String> interests, List<String> lifestyle) {

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(SEED_PASSWORD));
        user.setDisplayName(displayName);
        user.setDob(dob);
        user.setGender(gender);
        user.setCountry(country);
        user.setCity(city);
        user.setLanguages(languages);
        user.setMaritalStatus(maritalStatus);
        user.setProfession(profession);
        user.setBio(bio);
        user.setVerified(true);
        user = userRepository.save(user);

        Preference preference = new Preference();
        preference.setUserId(user.getId());
        preference.setIntentions(intentions);
        preference.setGeoScope(geoScope);
        preference.setInterests(interests);
        preference.setLifestyle(lifestyle);
        preferenceRepository.save(preference);
    }
}
