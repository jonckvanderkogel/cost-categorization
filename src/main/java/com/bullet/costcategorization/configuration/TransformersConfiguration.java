package com.bullet.costcategorization.configuration;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import com.bullet.costcategorization.service.Transformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Predicate;

import static com.bullet.costcategorization.domain.LineItem.TransactionType.AF;
import static com.bullet.costcategorization.domain.LineItem.TransactionType.BIJ;

@Configuration
public class TransformersConfiguration {
    @Bean
    public Transformer<LineItem, Category> chainedTransformers() {
        return food()
                .orElse(insurance())
                .orElse(subscriptions())
                .orElse(tax())
                .orElse(sports())
                .orElse(medical())
                .orElse(utilities())
                .orElse(mortgage())
                .orElse(salary())
                .orElse(savings());
    }

    private boolean descr(LineItem li, String test) {
        return li.getDescription().equals(test);
    }

    private boolean descrContains(LineItem li, String test) {
        return li.getDescription().toUpperCase().contains(test.toUpperCase());
    }

    private boolean trans(LineItem li, LineItem.TransactionType transactionType) {
        return li.getTransactionType().equals(transactionType);
    }

    private Transformer<LineItem, Category> transformer(Predicate<LineItem> predicate, Category category) {
        return (li) -> predicate.test(li) ? Optional.of(category) : Optional.empty();
    }

    private Transformer<LineItem, Category> mortgage() {
        Predicate<LineItem> mortgagePredicate = (li) -> descr(li,"ST ONTVANGST MUNT HYP") && trans(li, AF);

        return transformer(mortgagePredicate, Category.MORTGAGE);
    }

    private Transformer<LineItem, Category> insurance() {
        Predicate<LineItem> insurancePredicate = (li) ->
                descr(li, "DITZO") && trans(li, AF) ||
                        descr(li, "SCILDON N.V.") ||
                        descrContains(li, "InShared");

        return transformer(insurancePredicate, Category.INSURANCE);
    }

    private Transformer<LineItem, Category> tax() {
        Predicate<LineItem> taxPredicate = (li) ->
                descr(li, "BELASTINGDIENST") ||
                        descr(li, "SVB Utrecht");

        return transformer(taxPredicate, Category.TAX);
    }

    private Transformer<LineItem, Category> medical() {
        Predicate<LineItem> medicalPredicate = (li) ->
                descr(li, "DITZO ZORGVERZEKERING") ||
                        descrContains(li, "Apotheek") ||
                        descr(li, "Infomedics B.V.");

        return transformer(medicalPredicate, Category.MEDICAL);
    }

    private Transformer<LineItem, Category> subscriptions() {
        Predicate<LineItem> subscriptionsPredicate = (li) ->
                descr(li, "DPG MEDIA MAGAZINES BV") ||
                        descrContains(li,"THE RENT COMPANY") ||
                        descr(li, "TriNed") ||
                        descr(li, "SIMPEL NL BV") ||
                        descr(li, "Sanoma Media Netherlands BV") ||
                        descr(li, "KPN - Mobiel") ||
                        descr(li, "Vereniging Eigen Huis") ||
                        descr(li, "TriNed") ||
                        descr(li, "STICHTING DERDENGELDEN BUCKAROO") ||
                        descr(li, "Kosten BetaalPakket") ||
                        descr(li, "WARMGARANT BV");

        return transformer(subscriptionsPredicate, Category.SUBSCRIPTIONS);
    }

    private Transformer<LineItem, Category> sports() {
        Predicate<LineItem> sportsPredicate = (li) ->
                descr(li, "Silent Dragon") ||
                        descrContains(li, "Jumpsquare");

        return transformer(sportsPredicate, Category.SPORT);
    }

    private Transformer<LineItem, Category> utilities() {
        Predicate<LineItem> utilitiesPredicate = (li) ->
                descr(li, "OASEN NV") ||
                        descr(li, "NLE");

        return transformer(utilitiesPredicate, Category.UTILITIES);
    }

    private Transformer<LineItem, Category> food() {
        Predicate<LineItem> foodPredicate = (li) ->
                descr(li, "Crowdbutching.com B.V.") ||
                        descrContains(li,"Plus Koornneef") ||
                        descrContains(li, "Fris Vers") ||
                        descr(li, "CCVVOF De Noot WADDINXVEEN NLD") ||
                        descr(li, "markten Gouda REEUWIJK NLD") ||
                        descr(li, "Kaashandel Jelier vo DORDRECHT") ||
                        descrContains(li, "Lidl") ||
                        descrContains(li, "Picnic") ||
                        descrContains(li, "Sams Bakery") ||
                        descrContains(li, "Hassan Ait El Hadj") ||
                        descrContains(li, "Slagerij Lagendijk") ||
                        descr(li, "EurAzie Toko GOUDA NLD") ||
                        descrContains(li, "De vlaam gouda") ||
                        descrContains(li, "JumboSupermarktGouda") ||
                        descrContains(li, "ALBERT HEIJN");

        return transformer(foodPredicate, Category.FOOD);
    }

    private Transformer<LineItem, Category> salary() {
        Predicate<LineItem> salaryPredicate = (li) ->
                descr(li, "ING BANK NV INZAKE SALARISBETALING") ||
                        (li.getDescription().contains("GRAFISCH LYCEUM") && trans(li, BIJ));

        return transformer(salaryPredicate, Category.SALARY);
    }

    private Transformer<LineItem, Category> savings() {
        Predicate<LineItem> savingsPredicate = (li) ->
                descr(li, "Hr JI van der Kogel") ||
                        descr(li, "Mw F van der Kogel") ||
                        descr(li, "Floor van der Kogel");

        return transformer(savingsPredicate, Category.SAVINGS);
    }
}
