package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.*;
import app.nftguy.nftapi.cip25.MetaData;
import app.nftguy.nftapi.model.NftTransaction;
import app.nftguy.nftapi.model.NftTransactionDraft;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.nftstorage.NftStorageService;
import app.nftguy.nftapi.nftstorage.Responses.UploadResponse;
import app.nftguy.nftapi.nftstorage.Responses.Value;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;


@Component
public class NftCreateService {

    NftStorageService nftStorageService;
    AccountHelper accountHelper;
    BlockFrostHelper blockFrostHelper;
    PolicyHelper policyHelper;
    AssetHelper assetHelper;
    AddressHelper addressHelper;
    TransactionRepository transactionRepository;
    Config config;

    Logger logger = LoggerFactory.getLogger(NftCreateService.class);

    NftCreateService(
            AccountHelper accountHelper,
            BlockFrostHelper blockFrostHelper,
            NftStorageService nftStorageService,
            AddressHelper addressHelper,
            TransactionRepository transactionRepository,
            Config config){

        this.accountHelper = accountHelper;
        this.blockFrostHelper = blockFrostHelper;
        this.nftStorageService = nftStorageService;
        this.addressHelper = addressHelper;
        this.transactionRepository = transactionRepository;
        this.config = config;
    }

    public NftTransactionDraft draftTransaction(
            MultipartFile file,
            JSONObject userInput)
            throws Exception {

        JSONObject attributes = userInput.getJSONObject("attributes");
        NftTransaction nftTransaction = emptyTransaction(userInput);
        transactionRepository.save(nftTransaction);
        logger.info(String.format("New empty transaction. Id: %s, Name: %s, File type: %s, File name: %s",
                nftTransaction.getId(), attributes.getString("name"), file.getContentType(), file.getOriginalFilename()));
        uploadFile(file, attributes, nftTransaction.getId(), userInput.getString("receive_address"));
        return transactionRepository.findItemByIdRestricted(nftTransaction.getId());
    }

    private NftTransaction emptyTransaction(
            JSONObject userInput){
        return new NftTransaction(
                addressHelper.getNextAvailableAddress(),
                null,
                userInput.getString("receive_address"),
                null,
                config.getConfig().getCreateFee(),
                null);
    }
    private NftBuilder buildNft(
            MetaData metaData,
            String receiveAddress)
            throws JsonProcessingException, ApiException{

        return new NftBuilder(this.blockFrostHelper, this.accountHelper)
                .setMultiAsset(this.assetHelper.getMultiAsset())
                .setPolicy(this.policyHelper.getPolicy())
                .setMetaData(metaData)
                .setReceiveAddress(receiveAddress)
                .setTTL(config.getConfig().getTtl())
                .build();
    }

    private void uploadFile(
            MultipartFile file,
            JSONObject attributes,
            String nftTransactionId,
            String receiveAddress) throws IOException {

        NftTransaction nftTransaction = transactionRepository.findItemById(nftTransactionId);
        String nftName = attributes.getString("name");
        File tmpFile = File.createTempFile("nftguy", ".tmp");

        file.transferTo(tmpFile);
        nftStorageService.upload(tmpFile)
                .onErrorReturn(new UploadResponse(false, new Value()))
                .subscribe(uploadResponse -> {
                            processResponse(uploadResponse, attributes, nftName, receiveAddress, nftTransaction, tmpFile);
                        });
    }

    private void processResponse(
            UploadResponse uploadResponse,
            JSONObject attributes,
            String nftName,
            String receiveAddress,
            NftTransaction nftTransaction,
            File tmpFile){
            if(uploadResponse.getOk()){
                logger.info(String.format("Image upload cid %s", uploadResponse.getValue().getCid()));
               try {
                    initialiseComponents(nftName);
                    attributes.put("image",String.format("ipfs://%s",uploadResponse.getValue().getCid()));
                    NftBuilder nftBuilder =
                            buildNft(new MetaData(nftName,attributes, policyHelper.getPolicyId()), receiveAddress);
                    nftTransaction.setNetworkFee(nftBuilder.getFee());
                    nftTransaction.setTransactionCBORBytes(nftBuilder.getTransactionCBORBytes());
                    nftTransaction.setTtl(nftBuilder.getTtl());
                    nftTransaction.setPaymentState(PaymentState.PENDING);
                } catch (CborSerializationException | ApiException | JsonProcessingException e) {
                    nftTransaction.setPaymentState(PaymentState.FAILED);
                    logger.warn(String.format("Image upload error %s", e.toString()));
                    throw new RuntimeException(e);
                }
            }else{
                logger.info(String.format("Image upload failed"));
                nftTransaction.setPaymentState(PaymentState.FAILED);
            }
            transactionRepository.save(nftTransaction);
            tmpFile.delete();
    }

    private void initialiseComponents(String assetName) throws CborSerializationException {
        KeyHelper keyHelper = new KeyHelper();
        policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());
        assetHelper = new AssetHelper(
                assetName,
                BigInteger.valueOf(1L),
                policyHelper.getPolicyId());
    }

}
