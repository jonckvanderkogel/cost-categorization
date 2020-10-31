# cost-categorization
A project that categorizes your ING bank statements. 
To use:
* Download your bank statement CSV from https://mijn.ing.nl
* Create your own Categorizer bean. Not publishing mine for privacy reasons.
* After running the application, place your bank statement in the root of the directory and your line items will be categorized.
* Connect to http://localhost:8080 where your categorized line items will be streamed to a websocket.

# Creating your own Categorizers
To create your own Categorizers, register a bean with your custom Categorizers, for example something like this:
```Java
@Configuration
public class CategorizersConfiguration {
    @Bean
    public Categorizer chainedCategorizers() {
        return food()
                .orElse(insurance())
                .orElse(tax());
    }

    private boolean descr(LineItem li, String test) {
        return li.getDescription().equals(test);
    }

    private boolean trans(LineItem li, LineItem.TransactionType transactionType) {
        return li.getTransactionType().equals(transactionType);
    }

    private Categorizer categorizer (Predicate<LineItem> predicate, Category category) {
        return (li) -> predicate.test(li) ? Optional.of(category) : Optional.empty();
    }

    private Categorizer food() {
            Predicate<LineItem> foodPredicate = (li) ->
                    descr(li, "My Bakery") ||
                    li.getDescription().contains("Albert Heijn");
    
            return categorizer(foodPredicate, Category.FOOD);
        }

    private Categorizer insurance() {
        Predicate<LineItem> insurancePredicate = (li) -> descr(li,"My Insurance Company") && trans(li, AF);

        return categorizer(insurancePredicate, Category.INSURANCE);
    }

    private Categorizer tax() {
        Predicate<LineItem> taxPredicate = (li) -> descr(li, "BELASTINGDIENST");

        return categorizer(taxPredicate, Category.TAX);
    }
}
```
