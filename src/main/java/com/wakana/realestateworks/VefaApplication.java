package com.wakana.realestateworks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.wakana.realestateworks.enums.FeeEnum;
import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.repository.SubscriptionPlanRepository;
import com.wakana.realestateworks.repository.SubscriptionRepository;
import com.wakana.realestateworks.services.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.transaction.Transactional;

import java.io.File;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.wakana.samater.services" }) // Scanne les packages du JAR
@OpenAPIDefinition(info = @Info(title = "ukubhalisa", version = "1.0.0", description = "", termsOfService = "runcodeNow", contact = @Contact(name = "Wakana", email = "contactwakana@gmail.com"), license = @License(name = "Licence", url = "")), security = {
        @SecurityRequirement(name = "bearerAuth")
})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class VefaApplication implements CommandLineRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionRepository subscriptionRepository;

    public VefaApplication(SubscriptionPlanService subscriptionPlanService,
            SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionRepository subscriptionRepository) {
        this.subscriptionPlanService = subscriptionPlanService;

        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public static void main(String[] args) {
        /*
         * byte[] key = new byte[32];
         * new SecureRandom().nextBytes(key);
         * String secretKey = Base64.getEncoder().encodeToString(key);
         * System.out.println("Generated SECRET_KEY: " + secretKey);
         */
        SpringApplication.run(VefaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // createDefaultPlans();
    }

    @Transactional
    private void createDefaultPlans() {

        // 1️⃣ Supprimer les abonnements avant les plans

        // subscriptionRepository.deleteAll();
        // subscriptionRepository.deleteAllInBatch();
        // subscriptionPlanRepository.deleteAllInBatch();

        for (SubscriptionPlanEnum planEnum : SubscriptionPlanEnum.values()) {

            double basicPrice = 0;
            double premiumPrice = 0;
            int basicProjectLimit = 0;
            double basicDiscount = 0.05;
            double premiumDiscount = 0.15;

            // ⚙️ Définition des tarifs et limites selon le profil
            switch (planEnum) {
                case PROMOTEUR -> {
                    basicPrice = 15000;
                    premiumPrice = 30000;
                    basicProjectLimit = 5;
                }
                case SITE_MANAGER -> {
                    basicPrice = 12000;
                    premiumPrice = 25000;
                    basicProjectLimit = 4;
                }
                case SUPPLIER -> {
                    basicPrice = 10000;
                    premiumPrice = 20000;
                    basicProjectLimit = 3;
                }
                case SUBCONTRACTOR -> {
                    basicPrice = 8000;
                    premiumPrice = 16000;
                    basicProjectLimit = 3;
                }
                case WORKER -> {
                    basicPrice = 5000;
                    premiumPrice = 10000;
                    basicProjectLimit = 2;
                }
                case MOA -> {
                    basicPrice = 20000;
                    premiumPrice = 40000;
                    basicProjectLimit = 2;
                }
                case BET -> {
                    basicPrice = 18000;
                    premiumPrice = 35000;
                    basicProjectLimit = 3;
                }
                default -> {
                    basicPrice = 10000;
                    premiumPrice = 20000;
                    basicProjectLimit = 3;
                }
            }

            // 🧩 BASIC
            if (subscriptionPlanService.getPlansByName(planEnum).stream()
                    .noneMatch(p -> p.getLabel().equalsIgnoreCase("BASIC"))) {
                String basicDescription = String.join("\n",
                        ". Gestion jusqu’à 5 projets simultanés",
                        ". Accès au tableau de bord standard",
                        ". Support technique par email (jours ouvrables)",
                        ". Mises à jour automatiques incluses",
                        ". Rapport mensuel d’activité");

                subscriptionPlanService.createPlan(
                        planEnum,
                        "BASIC",
                        basicDescription,
                        basicPrice,
                        1,
                        basicProjectLimit,
                        false,
                        basicDiscount);

                System.out.println("✅ " + planEnum.name() + " BASIC plan created.");
            }

            // 🌟 PREMIUM
            if (subscriptionPlanService.getPlansByName(planEnum).stream()
                    .noneMatch(p -> p.getLabel().equalsIgnoreCase("PREMIUM"))) {

                String premiumDescription = String.join("\n",
                        ". Nombre illimité de projets",
                        ". Assistance complète 24h/24 et 7j/7",
                        ". Tableau de bord avancé avec statistiques détaillées",
                        ". Accès aux nouvelles fonctionnalités en avant-première",
                        ". Réductions exclusives sur les services partenaires",
                        ". Intégration API et automatisations personnalisées");

                subscriptionPlanService.createPlan(
                        planEnum,
                        "PREMIUM",
                        premiumDescription,
                        premiumPrice,
                        1,
                        0,
                        true,
                        premiumDiscount);

                System.out.println("🌟 " + planEnum.name() + " PREMIUM plan created.");
            }
        }
    }

}
