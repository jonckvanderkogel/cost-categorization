# cost-categorization
A project that categorizes your ING bank statements. 
To use:
* Download your bank statement CSV from https://mijn.ing.nl
* Create your own Transformer<LineItem,Category> bean. Not publishing mine for privacy reasons.
* After running the application, place your bank statement in the root of the directory and your line items will be categorized.
* Connect to http://localhost:8080 where your categorized line items will be streamed to a websocket.

# Creating your own Transformers
To create your own Transformers, register a bean with your custom Transformers, for example something like this:
```Java
@Configuration
public class TransformersConfiguration {
    @Bean
    public Transformer<LineItem, Category> chainedTransformers() {
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

    private Transformer<LineItem, Category> transformer (Predicate<LineItem> predicate, Category category) {
        return (li) -> predicate.test(li) ? Optional.of(category) : Optional.empty();
    }

    private Transformer<LineItem, Category> food() {
            Predicate<LineItem> foodPredicate = (li) ->
                    descr(li, "My Bakery") ||
                    li.getDescription().contains("Albert Heijn");
    
            return transformer(foodPredicate, Category.FOOD);
        }

    private Transformer<LineItem, Category> insurance() {
        Predicate<LineItem> insurancePredicate = (li) -> descr(li,"My Insurance Company") && trans(li, AF);

        return transformer(insurancePredicate, Category.INSURANCE);
    }

    private Transformer<LineItem, Category> tax() {
        Predicate<LineItem> taxPredicate = (li) -> descr(li, "BELASTINGDIENST");

        return transformer(taxPredicate, Category.TAX);
    }
}
```

# Demonstrating Reactive Streams
To see how the Reactive Stream is really able to keep the memory usage low, start the app as follows:
```
mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx30m"
```

Then have the application process a really big file and monitor the memory usage.