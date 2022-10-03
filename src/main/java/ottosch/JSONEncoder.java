package ottosch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class JSONEncoder {
	private String mnemonic;
	private char[] passphrase;
	private char[] apiKey;
	private String dojoUrl;
	private boolean testnet = false;
	
	private String type = "whirlpool.gui";
	private String version = "3.0.0";
	
	public JSONEncoder setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic.trim().replaceAll("\\s{2,}", " ");
		String[] tokens = this.mnemonic.split(" "); 
		if (tokens.length < 12 || tokens.length > 24 || tokens.length % 3 != 0) {
			System.err.println("Seed is invalid. Quitting...");
			System.exit(1);
		}
		return this;
	}

	public JSONEncoder setPassphrase(char[] passphrase) {
		this.passphrase = passphrase;
		return this;
	}

	public JSONEncoder setApiKey(char[] apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	public JSONEncoder setDojoUrl(String dojoUrl) {
		this.dojoUrl = dojoUrl.trim();
		return this;
	}
	
	public JSONEncoder setType(String type) {
		if (!type.isBlank()) {
			this.type = type.trim();
		}
		return this;
	}
	
	public JSONEncoder setVersion(String version) {
		if (!version.isBlank()) {
			this.version = version.trim();
		}
		return this;
	}
	
	public JSONEncoder setTestnet(boolean testnet) {
		this.testnet = testnet;
		return this;
	}
	
	public JSONEncoder setNetwork(String network) {
		network = network.trim();
		return setTestnet(network.equalsIgnoreCase("testnet") || network.equalsIgnoreCase("T"));
	}
	
	public JSONObject generate() {
		Map<String, Object> mainMap = new LinkedHashMap<>();

		Map<String, Object> pairingMap = new LinkedHashMap<>();
		pairingMap.put("type", type);
		pairingMap.put("version", version);
		pairingMap.put("network", testnet ? "testnet" : "mainnet");
		pairingMap.put("passphrase", true);
		pairingMap.put("mnemonic", AESUtil.encrypt(mnemonic, passphrase));
		mainMap.put("pairing", pairingMap);
		
		if (apiKey != null && apiKey.length > 0) {
			Map<String, Object> dojoMap = new LinkedHashMap<>();
			dojoMap.put("apikey", AESUtil.encrypt(apiKey, passphrase));
			dojoMap.put("url", dojoUrl);
			mainMap.put("dojo", dojoMap);
		}
		
		
		return new JSONObject(mainMap);
	}
}
