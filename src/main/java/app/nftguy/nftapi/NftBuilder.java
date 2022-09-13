package app.nftguy.nftapi;

import app.nftguy.nftapi.helper.AccountHelper;
import app.nftguy.nftapi.helper.BlockFrostHelper;
import app.nftguy.nftapi.cip25.MetaData;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.api.model.Result;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.client.transaction.model.MintTransaction;
import com.bloxbean.cardano.client.transaction.model.TransactionDetailsParams;
import com.bloxbean.cardano.client.transaction.spec.MultiAsset;
import com.bloxbean.cardano.client.transaction.spec.Policy;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NftBuilder {

    private MintTransaction mintTransaction;

    private CBORMetadata metaData;

    private TransactionDetailsParams detailsParams;

    private long ttlOffset;

    private static BlockFrostHelper blockFrostHelper;

    private final AccountHelper accountHelper;

    private MultiAsset multiAsset;

    private String receiveAddress;

    private Policy policy;

    private String transactionCBORBytes;


    public NftBuilder(BlockFrostHelper blockFrostHelper, AccountHelper accountHelper){
        NftBuilder.blockFrostHelper = blockFrostHelper;
        this.accountHelper = accountHelper;
    }

    public NftBuilder setMultiAsset(MultiAsset multiAsset){
        this.multiAsset = multiAsset;
        return this;
    }

    public NftBuilder setReceiveAddress(String address){
        this.receiveAddress = address;
        return this;
    }

    public NftBuilder setPolicy(Policy policy){
        this.policy = policy;
        return this;
    }

    public NftBuilder setMetaData(MetaData metaData) throws JsonProcessingException {
        this.metaData = metaData.getMetaData();
        return this;
    }

    public NftBuilder setTTL(long ttl) throws ApiException {
        this.ttlOffset = ttl;
        ttl = blockFrostHelper.getBlockService().getLatestBlock().getValue().getSlot() + ttlOffset;
        this.detailsParams =
                TransactionDetailsParams.builder()
                        .ttl(ttl)
                        .build();
        return this;
    }

    public NftBuilder build() {
        this.mintTransaction = MintTransaction.builder()
                        .sender(accountHelper.getAccount())
                        .receiver(receiveAddress)
                        .mintAssets(List.of(this.multiAsset))
                        .policy(policy)
                        .build();
        try {
            this.calculateFee();
            this.generateCBORBytes();

        } catch (ApiException | AddressExcepion | CborSerializationException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private void generateCBORBytes() throws AddressExcepion, CborSerializationException, ApiException {
        this.transactionCBORBytes = blockFrostHelper
                .getTxnHelperService()
                .getTransactionBuilder()
                .createSignedMintTransaction(this.mintTransaction, this.detailsParams, this.metaData);
    }

    public BigInteger calculateFee() throws AddressExcepion, CborSerializationException, ApiException {
        BigInteger fee = blockFrostHelper.getFeeCalcService().calculateFee(mintTransaction,
                detailsParams,
                metaData);
        mintTransaction.setFee(fee);
        return fee;
    }

    public BigInteger getFee(){
       return mintTransaction.getFee();
    }

    public long getTtl(){
        return detailsParams.getTtl();
    }

    public MintTransaction getMintTransaction() {
        return mintTransaction;
    }

    public String getTransactionCBORBytes() {
        return transactionCBORBytes;
    }

    public Result<TransactionResult> submit() {
        Result<TransactionResult> result = null;
        try {
            result = blockFrostHelper
                    .getTxnHelperService()
                    .mintToken(this.mintTransaction, this.detailsParams, this.metaData);
        } catch (AddressExcepion | ApiException | CborSerializationException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Result<String> submit(String transactionCBORBytes) throws ApiException {
        Result<String> result = null;
        try {
            result = blockFrostHelper
                    .getTxnService()
                    .submitTransaction(transactionCBORBytes.getBytes(StandardCharsets.UTF_8));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
