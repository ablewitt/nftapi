package app.nftguy.nftapi.helper;

import com.bloxbean.cardano.client.api.helper.FeeCalculationService;
import com.bloxbean.cardano.client.api.helper.TransactionHelperService;
import com.bloxbean.cardano.client.backend.api.*;
import com.bloxbean.cardano.client.backend.blockfrost.common.Constants;
import com.bloxbean.cardano.client.backend.blockfrost.service.BFBackendService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class BlockFrostHelper {

    BackendService backendService;
    TransactionHelperService txnHelperService;
    AddressService addressService;
    FeeCalculationService feeCalcService;
    TransactionService txnService;
    BlockService blockService;
    AssetService assetService;
    UtxoService utxoService;
    MetadataService metadataService;

    public BlockFrostHelper(Environment environment){
        String blockFrostProjKey = environment.getProperty("blockfrost.project_key");
        String network = environment.getProperty("cardano.network");
        String blockFrostNetwork =  Constants.BLOCKFROST_TESTNET_URL;
        if(network.equalsIgnoreCase("mainnet")){
            blockFrostNetwork =  Constants.BLOCKFROST_MAINNET_URL;
        }
        backendService =
                new BFBackendService(blockFrostNetwork, blockFrostProjKey);

        this.txnHelperService = backendService.getTransactionHelperService();
        this.addressService = backendService.getAddressService();
        this.feeCalcService = backendService.getFeeCalculationService();
        this.txnService = backendService.getTransactionService();
        this.blockService = backendService.getBlockService();
        this.assetService = backendService.getAssetService();
        this.utxoService = backendService.getUtxoService();
        this.metadataService = backendService.getMetadataService();
    }

    public BackendService getBackendService() {
        return backendService;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public TransactionHelperService getTxnHelperService() {
        return txnHelperService;
    }

    public void setTxnHelperService(TransactionHelperService txnHelperService) {
        this.txnHelperService = txnHelperService;
    }

    public FeeCalculationService getFeeCalcService() {
        return feeCalcService;
    }

    public TransactionService getTxnService() {
        return txnService;
    }


    public BlockService getBlockService() {
        return blockService;
    }


    public AssetService getAssetService() {
        return assetService;
    }


    public UtxoService getUtxoService() {
        return utxoService;
    }


    public MetadataService getMetadataService() {
        return metadataService;
    }


}
