package unipos.data.components.taxRates;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.data.shared.AbstractServiceTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by dominik on 28.07.15.
 */
public class TaxRateServiceTest extends AbstractServiceTest {

    @Mock
    TaxRateRepository taxRateRepository;

    @InjectMocks
    TaxRateService taxRateService = new TaxRateServiceImpl();

    @Test
    public void testFindAll() throws Exception {
        when(taxRateRepository.findAll()).thenReturn(Arrays.asList(new TaxRate("Entertainment",10, TaxRateCategory.NORMAL ,"Cinema"), new TaxRate("Drinking",20, TaxRateCategory.DISCOUNT, "Drink")));

        List<TaxRate> taxRateList = taxRateService.findAll();

        assertThat(taxRateList.size(), is(2));
        assertThat(taxRateList.get(0).getPercentage(), is(10));
        assertThat(taxRateList.get(0).getDescription(), is("Cinema"));
    }

    @Test
    public void testDeleteAllTaxRates() throws Exception {
        reset(taxRateRepository);

        doNothing().when(taxRateRepository).deleteAll();

        taxRateService.deleteAllTaxRates();

        verify(taxRateRepository, times(1)).deleteAll();
    }

    @Test
    public void testSaveTaxRate() throws Exception {
        reset(taxRateRepository);

        when(taxRateRepository.save(any(TaxRate.class))).thenReturn(null);

        taxRateService.saveTaxRate(new TaxRate("LOL", 12, TaxRateCategory.NORMAL, "LOL"));

        verify(taxRateRepository, times(1)).save(any(TaxRate.class));
    }

    @Test
    public void testDeleteTaxRatesWithExistingMongoDbId() throws Exception {
        reset(taxRateRepository);

        when(taxRateRepository.exists(anyString())).thenReturn(true);
        doNothing().when(taxRateRepository).delete(anyString());

        taxRateService.deleteTaxRateByMongoId("LOL");
        verify(taxRateRepository, times(1)).exists("LOL");
        verify(taxRateRepository,times(1)).delete("LOL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTaxRateWithNotExistingMongoDbId() throws Exception {
        reset(taxRateRepository);

        when(taxRateRepository.exists(anyString())).thenReturn(false);

        taxRateService.deleteTaxRateByMongoId("ASDF");

        verify(taxRateRepository, times(1)).exists("ASDF");
    }

}