package unipos.signature.components.signatureOption;

import org.apache.commons.io.IOUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.integritySafeGuard.SmartCardUtils;
import unipos.integritySafeGuard.domain.RksSuite;
import unipos.signature.components.dto.EncryptionDetailsDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by domin on 04.11.2016.
 */
@RestController
@RequestMapping(value = "/signatureOptions", produces = "application/json")
public class SignatureOptionController {

    @Autowired
    SignatureOptionService signatureOptionService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<SignatureOptionDto> findAll() {
        List<SignatureOption> signatureOptions = signatureOptionService.findAll();

        ModelMapper modelMapper = new ModelMapper();

        return signatureOptions.stream().map(x -> modelMapper.map(x, SignatureOptionDto.class)).collect(toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> saveSignatureOption(HttpServletRequest request, @RequestBody SignatureOptionDto signatureOption) {
        Assert.notNull(signatureOption, "SignatureOption must not be null!");
        Assert.notNull(signatureOption.secretKeyPlainText, "The secretKey must not null!");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<SignatureOptionDto, SignatureOption>() {
            @Override
            protected void configure() {
                using(new AbstractConverter<String, String>() {
                    @Override
                    protected String convert(String s) {
                        return s == null ? "" : s.replace("-", "").replace(" ", "").toUpperCase();
                    }
                }).map().setCrtSerialNo(source.getCrtSerialNo());
            }
        });

        SignatureOption option = modelMapper.map(signatureOption, SignatureOption.class);
        option.setRksSuite(RksSuite.valueOf(signatureOption.getRksSuite().replace("-", "_")));

        signatureOptionService.saveSignatureOption(option);

        return null;
    }

    @RequestMapping(value = "/storeGuid/{storeGuid}", method = RequestMethod.GET)
    public SignatureOption signatureOption(@PathVariable("storeGuid") String storeGuid, HttpServletResponse response) {
        Assert.notNull(storeGuid, "The store Guid must not be null!");

        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(storeGuid);

        if (signatureOption == null) {
            response.setStatus(400);
        }
        return signatureOption;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteByGuid(@RequestParam("storeGuid") String storeGuid) {
        Assert.isTrue(storeGuid != null && !storeGuid.isEmpty(), "No valid storeGuid value provided: " + storeGuid);

        signatureOptionService.deleteByStoreGuid(storeGuid);
    }

    @RequestMapping(value = "/isFirstSignatureOptionForStore", method = RequestMethod.GET)
    public boolean isFirstSignatureOptionForStore(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        Assert.notNull(store, "No store found for you current Device- and Auth-Token");

        //If no signatureJob is found for the given store, return true!!! -> Next one is the first one.
        return signatureOptionService.findByStoreGuid(store.getGuid()) == null;
    }

    @RequestMapping(value = "/forUserAndDevice", method = RequestMethod.GET)
    public SignatureOption getSignatureOptionForDeviceAndUser(HttpServletRequest request) throws Exception {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        Assert.notNull(store, "No store found for the given request cookies!");
        Assert.notNull(store.getGuid(), "The given store has not valid guid assigned. Was " + store.getGuid());

        return signatureOptionService.findByStoreGuid(store.getGuid());
    }

    @RequestMapping(value = "/encryptionDetails", method = RequestMethod.GET)
    public EncryptionDetailsDto getEncryptionDetails(@RequestParam String id) {
        SignatureOption signatureOption = signatureOptionService.findById(id);
        Assert.notNull(signatureOption, "No signatureOption found for the given id: " +  id);

        EncryptionDetailsDto encryptionDetailsDto = new EncryptionDetailsDto();
        encryptionDetailsDto.setPwPlainText(signatureOption.getSecretKeyPlainText());
        encryptionDetailsDto.setEncodedPassword(Base64.getEncoder().encodeToString(SmartCardUtils.getSecretKeyByCustomPw(signatureOption.getSecretKeyPlainText()).getEncoded()));
        encryptionDetailsDto.setVerificationValue(SmartCardUtils.calcCheckSumForKey(encryptionDetailsDto.getEncodedPassword(), signatureOption.rksSuite));

        return encryptionDetailsDto;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadCurrentSignatureSettings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No valid store found for the given Auth- and Device-Token");

        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(store.getGuid());
        Assert.notNull(signatureOption, "No SignaturOption found for the given storeGuid: " + store.getGuid());

        String base64EncryptedKey = Base64.getEncoder().encodeToString(SmartCardUtils.getSecretKeyByCustomPw(signatureOption.getSecretKeyPlainText()).getEncoded());

        List<String> downloadString = new ArrayList<>();
        downloadString.add("Kassa-ID: " + signatureOption.getKassaId());
        downloadString.add("Verschlüsselungs-PW - Kodiert: " + base64EncryptedKey);
        downloadString.add("Verifizierungswert: " + SmartCardUtils.calcCheckSumForKey(base64EncryptedKey, signatureOption.rksSuite));
        downloadString.add("Zertifikat-Seriennummer: " + signatureOption.getCrtSerialNo());

        OutputStream os = response.getOutputStream();
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"Finanzamt-Details.txt\"");

        IOUtils.write(downloadString.stream().collect(Collectors.joining("\n")), os);
        os.flush();
    }
}
