package unipos.pos.components.dailySettlement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.report.DailySettlementHelper;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.cashbook.CashbookEntryService;
import unipos.pos.shared.AbstractServiceTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 19.01.2016.
 */
public class DailySettlementServiceTest extends AbstractServiceTest {

    @Mock
    DailySettlementRepository dailySettlementRepository;
    @InjectMocks
    DailySettlementService dailySettlementService = new DailySettlementServiceImpl();
    @Mock
    Environment environment;
    @Mock
    ReportRemoteInterface reportRemoteInterface;
    @Mock
    DataRemoteInterface dataRemoteInterface;
    @Mock
    CashbookEntryService cashbookEntryService;

    DailySettlement dailySettlement;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        dailySettlement = DailySettlement.builder()
                .closed(false)
                .guid(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .build();
    }

    @Test
    public void testAddNewSettlement() throws Exception {

        when(dailySettlementRepository.save(any(DailySettlement.class))).thenReturn(null);
        when(dailySettlementRepository.findLastByStoreGuidAndDeviceIdOrderByDateTimeDesc(anyString(), anyString())).thenReturn(null);
        when(environment.getProperty(eq("dailySettlementMode"))).thenReturn("device");

        DailySettlement dailySettlement = dailySettlementService.addNewSettlement(Store.builder().companyGuid("companyGuid").build(), Workstation.builder().deviceId("deviceId").build());

        assertThat(dailySettlement.getGuid().isEmpty(), is(false));
        assertThat(dailySettlement.getDateTime(), is(notNullValue()));
        assertThat(dailySettlement.isClosed(), is(false));
    }

    @Test
    public void testAddNewSettlementWithLocalDateTime() throws Exception {
        when(dailySettlementRepository.save(any(DailySettlement.class))).thenReturn(null);
        when(environment.getProperty(eq("dailySettlementMode"))).thenReturn("device");
        when(dailySettlementRepository.findLastByStoreGuidAndDeviceIdOrderByDateTimeDesc(anyString(), anyString())).thenReturn(null);


        DailySettlement dailySettlement = dailySettlementService.addNewSettlementByTime(LocalDateTime.of(1994, 11, 5, 0, 35), Store.builder().companyGuid("companyGuid").build(), Workstation.builder().deviceId("deviceId").build());

        assertThat(dailySettlement.getDateTime().isEqual(LocalDateTime.of(1994, 11, 5, 0, 35)), is(true));
        assertThat(dailySettlement.getGuid().isEmpty(), is(false));
        assertThat(dailySettlement.isClosed(), is(false));
    }

    @Test
    public void testIsOpen() throws Exception {
        when(dailySettlementRepository.findByGuid(eq("guid"))).thenReturn(DailySettlement.builder()
                .closed(false)
                .dateTime(LocalDateTime.now())
                .guid("guid")
                .build());

        assertThat(dailySettlementService.isOpen("guid"), is(true));

        verify(dailySettlementRepository).findByGuid(eq("guid"));
    }

    @Test
    public void testIsOpenWithInstance() throws Exception {
        DailySettlement dailySettlement = DailySettlement.builder()
                .closed(false)
                .dateTime(LocalDateTime.now())
                .guid("guid")
                .build();

        when(dailySettlementRepository.findByGuid("guid")).thenReturn(dailySettlement);

        assertThat(dailySettlementService.isOpen(dailySettlement), is(true));

        verify(dailySettlementRepository).findByGuid(eq("guid"));
    }

    @Test
    public void testCloseSettlement() throws Exception {

        DailySettlement dailySettlement1 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 1, 0, 0)).build();
        DailySettlement dailySettlement2 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 1, 0, 0)).build();
        DailySettlement dailySettlement3 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 2, 0, 0)).build();
        DailySettlement dailySettlement4 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 3, 0, 0)).build();
        DailySettlement dailySettlement5 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 4, 0, 0)).build();
        DailySettlement dailySettlement6 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 4, 0, 0)).build();

        when(dailySettlementRepository.findByGuid(eq("guid"))).thenReturn(DailySettlement.builder()
                .dateTime(LocalDateTime.of(1994, 11, 5, 0, 35))
                .closed(false)
                .guid("guid")
                .deviceId("asdf")
                .storeGuid("guid")
                .build());

        when(dailySettlementRepository.findByStoreGuid(eq("guid"))).thenReturn(Arrays.asList(dailySettlement1, dailySettlement4, dailySettlement6, dailySettlement2, dailySettlement5, dailySettlement3));

        when(dataRemoteInterface.getStoreByGuid(eq("guid"))).thenReturn(Store.builder().build());

        doNothing().when(reportRemoteInterface).executeDailySettlement(eq(DailySettlementHelper.builder().startDate(LocalDateTime.of(2016, 1, 4, 0, 0)).endDate(LocalDateTime.now()).storeGuid("guid").build()));

        dailySettlementService.closeSettlement("guid");

        verify(dailySettlementRepository).save(argThat(new ArgumentMatcher<DailySettlement>() {

            @Override
            public boolean matches(Object o) {
                DailySettlement dailySettlement = (DailySettlement) o;

                return dailySettlement.getDateTime() != null
                        && dailySettlement.getGuid().equals("guid")
                        && dailySettlement.isClosed();
            }
        }));
    }

    @Test
    public void testGetLastClosedDailySettlementByStoreGuid() {

        DailySettlement dailySettlement1 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 1, 0, 0)).build();
        DailySettlement dailySettlement2 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 1, 0, 0)).build();
        DailySettlement dailySettlement3 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 2, 0, 0)).build();
        DailySettlement dailySettlement4 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 3, 0, 0)).build();
        DailySettlement dailySettlement5 = DailySettlement.builder().closed(false).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 4, 0, 0)).build();
        DailySettlement dailySettlement6 = DailySettlement.builder().closed(true).storeGuid("guid").dateTime(LocalDateTime.of(2016, 1, 4, 0, 0)).build();

        when(dailySettlementRepository.findByStoreGuid(eq("guid"))).thenReturn(Arrays.asList(dailySettlement1, dailySettlement4, dailySettlement6, dailySettlement2, dailySettlement5, dailySettlement3));

        DailySettlement dailySettlement = dailySettlementService.getLastClosedDailySettlementByStore("guid");

        assertThat(dailySettlement.isClosed(), is(true));
        assertThat(dailySettlement.getDateTime(), is(LocalDateTime.of(2016,1,4,0,0)));

//        when(dailySettlementService.getLastClosedDailySettlementByStore(any(Store))).thenReturn()
    }
}