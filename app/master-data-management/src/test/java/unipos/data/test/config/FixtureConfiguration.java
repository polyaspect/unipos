package unipos.data.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.data.components.category.CategoryFixture;
import unipos.data.components.company.CompanyFixture;
import unipos.data.components.paymentMethods.PaymentMethodFixture;
import unipos.data.components.product.ProductFixture;
import unipos.data.components.entity.EntityFixture;
import unipos.data.components.scope.ScopeFixture;
import unipos.data.components.taxRates.TaxRateFixture;

/**
 * @author ggradnig
 */
@Configuration
public class FixtureConfiguration {

    @Bean
    public EntityFixture entityFixture(){
        return new EntityFixture();
    }

    @Bean
    public ProductFixture productFixture() {
        return new ProductFixture();
    }

    @Bean
    public TaxRateFixture taxRateFixture() {
        return new TaxRateFixture();
    }

    @Bean
    public CategoryFixture categoryFixture() {
        return new CategoryFixture();
    }

    @Bean
    public PaymentMethodFixture paymentMethodFixture() {
        return new PaymentMethodFixture();
    }

    @Bean
    public CompanyFixture companyFixture() {
        return new CompanyFixture();
    }

    @Bean
    public ScopeFixture scopeFixture() {
        return new ScopeFixture();
    }
}
