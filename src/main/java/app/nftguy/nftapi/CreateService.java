package app.nftguy.nftapi;

import app.nftguy.nftapi.cip25.MetaData;
import app.nftguy.nftapi.helper.*;
import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.Transaction;
import app.nftguy.nftapi.model.TransactionClientModel;
import app.nftguy.nftapi.nftstorage.NftStorageService;
import app.nftguy.nftapi.nftstorage.Responses.UploadResponse;
import app.nftguy.nftapi.nftstorage.Responses.Value;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CreateService {

  NftStorageService nftStorageService;
  AccountHelper accountHelper;
  BlockFrostHelper blockFrostHelper;
  PolicyHelper policyHelper;
  AssetHelper assetHelper;
  AddressHelper addressHelper;
  TransactionRepository transactionRepository;
  Config config;

  Logger logger = LoggerFactory.getLogger(CreateService.class);

  CreateService(
      AccountHelper accountHelper,
      BlockFrostHelper blockFrostHelper,
      NftStorageService nftStorageService,
      AddressHelper addressHelper,
      TransactionRepository transactionRepository,
      Config config) {

    this.accountHelper = accountHelper;
    this.blockFrostHelper = blockFrostHelper;
    this.nftStorageService = nftStorageService;
    this.addressHelper = addressHelper;
    this.transactionRepository = transactionRepository;
    this.config = config;
  }

  public TransactionClientModel primeTransaction(MultipartFile file, JSONObject userInput)
      throws Exception {

    JSONObject attributes = userInput.getJSONObject("attributes");
    Transaction transaction = draftTransaction(userInput);
    if (userInput.has("email") && !userInput.getString("email").isEmpty()) {
      transaction.setEmail(userInput.getString("email"));
    }
    transactionRepository.save(transaction);
    logger.info(
        String.format(
            "New empty transaction. Id: %s, Name: %s, File type: %s, File name: %s",
            transaction.getId(),
            attributes.getString("name"),
            file.getContentType(),
            file.getOriginalFilename()));
    uploadFile(file, attributes, transaction.getId(), userInput.getString("receive_address"));
    return transactionRepository.createTransactionClientModelById(transaction.getId());
  }

  private Transaction draftTransaction(JSONObject userInput) {
    return new Transaction(
        addressHelper.getNextAvailableAddress(),
        null,
        userInput.getString("receive_address"),
        null,
        config.getConfig().getCreateFee(),
        null);
  }

  private NftBuilder buildNft(MetaData metaData, String receiveAddress)
      throws JsonProcessingException, ApiException {

    return new NftBuilder(this.blockFrostHelper, this.accountHelper)
        .setMultiAsset(this.assetHelper.getMultiAsset())
        .setPolicy(this.policyHelper.getPolicy())
        .setMetaData(metaData)
        .setReceiveAddress(receiveAddress)
        .setTTL(config.getConfig().getTtl())
        .build();
  }

  private void uploadFile(
      MultipartFile file, JSONObject attributes, String nftTransactionId, String receiveAddress)
      throws IOException {

    Transaction transaction = transactionRepository.findTransactionById(nftTransactionId);
    String nftName = attributes.getString("name");
    File tmpFile = File.createTempFile("nftguy", ".tmp");

    file.transferTo(tmpFile);
    nftStorageService
        .upload(tmpFile)
        .onErrorReturn(new UploadResponse(false, new Value()))
        .subscribe(
            uploadResponse -> {
              if (uploadResponse.getOk()) {
                logger.info(
                    String.format("Image upload cid %s", uploadResponse.getValue().getCid()));
                try {
                  initMintingComponents(nftName);
                  JSONArray ipfsLink = new JSONArray();
                  ipfsLink.put("ipfs://");
                  ipfsLink.put(uploadResponse.getValue().getCid());
                  attributes.put("image", ipfsLink);
                  NftBuilder nftBuilder =
                      buildNft(
                          new MetaData(nftName, attributes, policyHelper.getPolicyId()),
                          receiveAddress);
                  transaction.setNetworkFee(nftBuilder.getFee());
                  transaction.setTransactionCBORBytes(nftBuilder.getTransactionCBORBytes());
                  transaction.setTtl(nftBuilder.getTtl());
                  transaction.setPaymentState(PaymentState.PENDING);
                } catch (CborSerializationException | ApiException | JsonProcessingException e) {
                  transaction.setPaymentState(PaymentState.FAILED);
                  logger.warn(String.format("Image upload error %s", e.toString()));
                  throw new RuntimeException(e);
                }
              } else {
                logger.info(String.format("Image upload failed"));
                transaction.setPaymentState(PaymentState.FAILED);
              }
              transactionRepository.save(transaction);
              tmpFile.delete();
            });
  }

  private void initMintingComponents(String assetName) throws CborSerializationException {
    KeyHelper keyHelper = new KeyHelper();
    policyHelper = new PolicyHelper(keyHelper.getVKey(), keyHelper.getSKey());
    assetHelper = new AssetHelper(assetName, BigInteger.valueOf(1L), policyHelper.getPolicyId());
  }
}
